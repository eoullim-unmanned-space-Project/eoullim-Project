package org.example.eoullimback.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.booking.BookingRepository;
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

    @Value("${portone.imp-key}")
    private String impKey;

    @Value("${portone.imp-secret-key}")
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
    public void complete(String impUid, String merchantUid) {

        Payment paymentEntity = paymentRepository.findByPaymentKey(merchantUid)
                .orElseThrow(() -> new Exception404(ErrorCode.PAYMENT_NOT_FOUND));

        String accessToken = getPortOneToken();

        PaymentResponse.PaymentData body = getPaymentData(impUid, accessToken);

        Long amount = body.getAmount();
        String status = body.getStatus();
        String failedMessage = body.getFailReason();

        if (!paymentEntity.getAmount().equals(amount)) {
            throw new RuntimeException("결제 금액 불일치 ! : 위변조 주의");
        }

        List<Booking> bookingEntities = bookingRepository.findAllByBookingCode(paymentEntity.getOrderId())
                .orElseThrow(() -> new Exception404(ErrorCode.BOOKING_CODE_NOT_FOUND));

       switch (status) {
            // 추후 qrcode 전송 로직
           case "paid" :
               paymentEntity.markSuccess(impUid);

                for (Booking booking : bookingEntities) {
                    booking.changeSuccess();
                }

               log.info("결제 및 예약 확정 완료 : {}", merchantUid);
               break;

           case "failed" :
               paymentEntity.markFailed(
                    "PORTONE_PAYMENT_FAILED",
                       failedMessage
               );
               break;

           case "cancelled" :
               paymentEntity.markFailed(
                       "USER_CANCEL",
                       "사용자가 결제를 취소했습니다."
               );
               paymentEntity.getBooking().changeCanceled();
               break;

           default:
               throw new RuntimeException("처리되지 않은 결제 상태 [" + status + "] 입니다. 고객센터에 문의하세요.");
       }
    }

    private String getPortOneToken() {
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

        log.info("response = response={}", response);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalArgumentException("PortOne 토큰 발급에 실패했습니다. 실패: response.body가 비어있습니다.");
        }

        return response.getBody().getResponse().getAccessToken();
    }

    private PaymentResponse.PaymentData getPaymentData(String impUid, String accessToken ) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PaymentResponse.PortOnePaymentDetailDTO> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, PaymentResponse.PortOnePaymentDetailDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalArgumentException("상세보기를 조회하지 못했습니다.");
        }

        PaymentResponse.PortOnePaymentDetailDTO body = response.getBody();

        if (body == null) {
            throw new Exception404(ErrorCode.PAYMENT_DAYA_NOT_FOUND);
        }

        return body.getResponse();
    }


    private String generatePaymentKey(Long id) {
        return "payment-" + id + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
