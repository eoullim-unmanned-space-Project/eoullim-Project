package org.example.eoullimback.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.error.exception.*;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.booking.BookingRepository;
import org.example.eoullimback.notification.NotificationService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Value("${portone.imp-key}")
    private String impKey;

    @Value("${portone.imp-secret}")
    private String impSecret;

    @Override
    @Transactional
    public PaymentResponse.PrepareDTO prepare(Long id, String bookingCode) {
    
        // 사용자 존재 여부
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        // 부킹 존재 여부
        List<Booking> bookingEntities = bookingRepository.findAllByBookingCode(bookingCode)
                .orElseThrow(() ->  new Exception404(ErrorCode.BOOKING_NOT_FOUND));

        Booking booking = bookingEntities.get(0);

        // 부킹의 사용자가 결제한 해당 사용자가 맞는지 검증
        if (!booking.getUser().getId().equals(id)) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        // 부킹의 상태값 확인 대기중이 아니면 예외처리
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new Exception400(ErrorCode.INVALID_BOOKING_STATUS);
        }

        if (paymentRepository.existsByBooking(booking)) {
           throw new Exception400(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }
        
        String paymentKey = generatePaymentKey(id);

        while (paymentRepository.existsByPaymentKey(paymentKey)) {
            paymentKey = generatePaymentKey(id);
        }

        Payment payment = Payment.builder()
                .user(userEntity)
                .booking(booking)
                .orderId(booking.getBookingCode())
                .paymentKey(paymentKey)
                .amount(booking.getItemSnapshotPrice())
                .method(PaymentMethod.KAKAO_PAY)
                .status(PaymentStatus.READY)
                .productName(booking.getRoom().getName())
                .build();

        paymentRepository.save(payment);

        return new PaymentResponse.PrepareDTO(
                payment.getPaymentKey(),
                payment.getAmount()
        );
    }

    @Override
    @Transactional
    public String complete(Long userId, String impUid, String merchantUid) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        Payment paymentEntity = paymentRepository.findByPaymentKey(merchantUid)
                .orElseThrow(() -> new Exception404(ErrorCode.PAYMENT_NOT_FOUND));

        // 중복 방지
        if (paymentRepository.findByImpUid(impUid).isPresent()) {
            throw new Exception400(ErrorCode.PAYMENT_COMPLETED);
        }

        String accessToken = getPortOneToken();

        PaymentResponse.PaymentData body = getPaymentData(impUid, accessToken);

        Long amount = body.getAmount();
        String status = body.getStatus();
        String failedMessage = body.getFailReason();

        if (!paymentEntity.getAmount().equals(amount)) {
            log.error("[결제 금액 불일치 위변조 감지] 주문번호: {}, 저장된 금액: {}, 들어온 금액: {}", paymentEntity.getId(), paymentEntity.getAmount(), amount);
            throw new Exception400(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        List<Booking> bookingEntities = bookingRepository.findAllByBookingCode(paymentEntity.getOrderId())
                .orElseThrow(() -> new Exception404(ErrorCode.BOOKING_CODE_NOT_FOUND));

        switch (status) {
            // 추후 qrcode 전송 로직
            case "paid" :
                paymentEntity.markSuccess(impUid);

                for (Booking booking : bookingEntities) {
                    booking.changeSuccess();
                    log.info("부킹 예약 완료 상태 변경되었습니다. 부킹코드: {}, 부킹상태: {}", booking.getBookingCode(), booking.getStatus());
                }

                notificationService.notifyPaymentSuccess(paymentEntity);
                log.info("결제 및 예약 확정 완료 되었습니다. 유저 ID: {}, 주문번호: {}, 결제금액: {}", user.getId(), paymentEntity.getId(), paymentEntity.getAmount());
                break;

            case "failed" :
                handlePaymentFailure(paymentEntity, bookingEntities, "PORTONE_PAYMENT_FAILED", failedMessage);
                notificationService.notifyPaymentFailed(paymentEntity, failedMessage);
                break;

            case "cancelled" :
                handlePaymentFailure(paymentEntity, bookingEntities, "PORTONE_PAYMENT_CANCELLED", "사용자가 결제를 취소하였습니다.");
                notificationService.notifyPaymentCancelled(paymentEntity);
                break;

            default:
                throw new Exception500(ErrorCode.INTERNAL_ERROR);
        }

        return paymentEntity.getOrderId();
    }

    private String getPortOneToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://api.iamport.kr/users/getToken";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();

            body.put("imp_key", impKey);
            body.put("imp_secret", impSecret);

            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(body, headers);

            ResponseEntity<PaymentResponse.PortOneDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    PaymentResponse.PortOneDTO.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalArgumentException("PortOne 토큰 발급에 실패했습니다. 실패: response.body가 비어있습니다.");
            }

            return response.getBody().getResponse().getAccessToken();

        } catch (Exception e) {
            log.error("포트원 토큰 발급 중 오류 발생: ", e);
            throw new Exception400(ErrorCode.PAYMENT_FAILED);
        }
    }

    private PaymentResponse.PaymentData getPaymentData(String impUid, String accessToken ) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://api.iamport.kr/payments/" + impUid;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<PaymentResponse.PortOnePaymentDetailDTO> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, PaymentResponse.PortOnePaymentDetailDTO.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalArgumentException("상세보기를 조회하지 못했습니다.");
            }

            PaymentResponse.PortOnePaymentDetailDTO body = response.getBody();

            if (body == null) {
                throw new Exception404(ErrorCode.PAYMENT_DATA_NOT_FOUND);
            }

            return body.getResponse();

        } catch (Exception e) {
            log.error("포트원 결제조회 중 오류 발생: ", e);
            throw new Exception400(ErrorCode.PAYMENT_FAILED);
        }
    }

    @Override
    @Transactional
    public void cancel(String paymentKey) {

        Payment paymentEntity = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new Exception400(ErrorCode.PAYMENT_NOT_FOUND));

        List<Booking> bookingEntities = bookingRepository.findAllByBookingCode(paymentEntity.getOrderId())
                .orElseThrow(() -> new Exception404(ErrorCode.BOOKING_CODE_NOT_FOUND));

        handlePaymentFailure(paymentEntity, bookingEntities, "PORTONE_PAYMENT_CANCELLED", "사용자가 결제를 취소하였습니다.");
    }

    private void handlePaymentFailure(Payment payment, List<Booking> bookings, String code, String failMessage) {

        for (Booking booking : bookings) {
            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new Exception400(ErrorCode.INVALID_BOOKING_STATUS);
            }

            booking.changeCanceled();
            booking.getTimeSlot().open();

            log.info("예약 취소 및 타임슬롯 복구 완료: 예약코드: {}, 예약상태: {}, 타임슬롯ID: {}, 타임슬롯 상태: {}",
                    booking.getBookingCode(),
                    booking.getStatus(),
                    booking.getTimeSlot().getId(),
                    booking.getTimeSlot().getStatus());
        }

        payment.markFailed(code, failMessage);
    }

    private String generatePaymentKey(Long id) {
        return "payment-" + id + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
