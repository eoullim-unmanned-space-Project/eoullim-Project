package org.example.eoullimback.payment_refund;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.booking.BookingRepository;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment.PaymentRepository;
import org.example.eoullimback.payment.PaymentResponse;
import org.example.eoullimback.timeslot.TimeSlotResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentRefundServiceImpl implements PaymentRefundService{

    @Value("${portone.imp-key}")
    private String impKey;

    @Value("${portone.imp-secret}")
    private String impSecret;

    private final PaymentRefundRepository paymentRefundRepository;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void createRefund(String paymentKey, String reason, Long userId) {

        Payment payment = paymentRefundRepository.findByUserIdAndPaymentKey(userId, paymentKey)
                .orElseThrow(() -> new Exception400(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new Exception400(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        if (paymentRefundRepository.existsByPayment(payment)) {
            throw new Exception400(ErrorCode.ALREADY_REFUNDED);
        }

        PaymentRefund paymentRefund = PaymentRefund.builder()
                .payment(payment)
                .amount(payment.getAmount())
                .reason(reason)
                .status(RefundStatus.REQUESTED)
                .build();

        paymentRefundRepository.save(paymentRefund);
    }

    @Override
    public List<PaymentRefundResponse.AdminListDTO> getRefundList() {

        List<PaymentRefund> refundEntities = paymentRefundRepository.findAllWithPayment();

        List<Booking> bookingEntities = bookingRepository.findAllWithTimeSlot();

        Map<String, List<TimeSlotResponse.DetailDTO>> slotsMap = bookingEntities.stream()
                .collect(Collectors.groupingBy(
                        Booking::getBookingCode,
                        Collectors.mapping(b -> new TimeSlotResponse.DetailDTO(b.getTimeSlot()), Collectors.toList())
                ));

        return refundEntities.stream()
                .map(refund -> {
                    String bookingCode = refund.getPayment().getOrderId(); // 혹은 bookingCode 필드
                    List<TimeSlotResponse.DetailDTO> slots = slotsMap.getOrDefault(bookingCode, List.of());
                    return new PaymentRefundResponse.AdminListDTO(refund, slots);
                })
                .toList();
    }

    @Override
    public PaymentRefundResponse.AdminDetailDTO detail(Long id) {

        PaymentRefund refundEntity = paymentRefundRepository.findByIdPayment(id)
                .orElseThrow(() -> new Exception404(ErrorCode.PAYMENT_REFUND_NOT_FOUND));

        String bookingCode = refundEntity.getPayment().getOrderId();
        List<Booking> bookingEntities = bookingRepository.findAllByBookingCodeWithTimeSlot(bookingCode)
                .orElseThrow(() -> new Exception404(ErrorCode.TIMESLOT_NOT_FOUND));

        List<TimeSlotResponse.DetailDTO> timeSlots = bookingEntities.stream()
                .map(booking -> new TimeSlotResponse.DetailDTO(booking.getTimeSlot()))
                .toList();

        return new PaymentRefundResponse.AdminDetailDTO(refundEntity, timeSlots);
    }

    @Override
    @Transactional
    public void rejection(Long id, String reason) {
        PaymentRefund refundEntity = paymentRefundRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.PAYMENT_REFUND_NOT_FOUND));

        if (!refundEntity.checkRequest()) {
            throw new Exception400(ErrorCode.INVALID_REFUND_STATUS);
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new Exception400(ErrorCode.EMPTY_REASON);
        }

        Payment payment = refundEntity.getPayment();

        refundEntity.markReject(reason);

        payment.markCompleted();
    }

    @Override
    @Transactional
    public void approve(Long id) {

        PaymentRefund refundEntity = paymentRefundRepository.findByIdPayment(id)
                .orElseThrow(() -> new Exception404(ErrorCode.PAYMENT_REFUND_NOT_FOUND));

        if (!refundEntity.checkRequest()) {
            throw new Exception400(ErrorCode.INVALID_REFUND_STATUS);
        }

        Payment payment = refundEntity.getPayment();
        Long amount = payment.getAmount();

        cancelPortOne(payment.getImpUid(), amount);

        payment.markRefunded();
        refundEntity.markCompleted();

        List<Booking> bookingEntities = bookingRepository.findAllByBookingCodeWithTimeSlot(payment.getBooking().getBookingCode())
                .orElseThrow(() -> new Exception404(ErrorCode.TIMESLOT_NOT_FOUND));

            for (Booking booking : bookingEntities) {
                    booking.changeRefund();
                    booking.getTimeSlot().open();
                    log.info("환불이 정상적으로 처리되었습니다. 예약 상태:{}, 타임슬롯 상태:{}", booking.getStatus(), booking.getTimeSlot().getStatus());
            }
    }

    private void cancelPortOne(String impUid, Long amount) {
        
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("imp_uid", impUid);
        body.put("amount", amount);
        body.put("reason", "관리자 환불 승인");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.iamport.kr/payments/cancel",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                throw new Exception500(ErrorCode.FAIL_PORT_ONE_RESPONSE);
            }

            Integer code = (Integer) responseBody.get("code");

            if (code != 0) {
                throw new Exception400(ErrorCode.FAILED_REFUND);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

    private String getAccessToken() {
        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("imp_key", impKey);
            body.put("imp_secret", impSecret);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<PaymentResponse.PortOneDTO> response = restTemplate.exchange(
                    "https://api.iamport.kr/users/getToken",
                    HttpMethod.POST,
                    request,
                    PaymentResponse.PortOneDTO.class
            );

            return response.getBody().getResponse().getAccessToken();
        } catch (Exception e) {
            throw new Exception400(ErrorCode.PORT_ONE_ERROR);
        }
    }


        @Override
        public Long countPaymentsInRefundRequested() {
            return paymentRefundRepository.countPaymentsInRefundRequested();
        }

        // 환불 카테고리 카운트
        public List<PaymentRefundResponse.RefundCategoryCountDTO> getRefundCategoryCounts() {
            List<Object[]> rawResults = paymentRefundRepository.countRefundByCategory();
            System.out.println("==================================================================");
            System.out.println("rawResults size = " + rawResults.size());
            for(Object[] row: rawResults) {
                System.out.println("category = " + row[0] + ", count = " + row[1]);
            }


            List<PaymentRefundResponse.RefundCategoryCountDTO> result = rawResults.stream()
                    .map(row -> new PaymentRefundResponse.RefundCategoryCountDTO(
                            row[0] != null ? row[0].toString() : "UNKNOWN",
                            ((Number) row[1]).longValue()
                    ))
                    .collect(Collectors.toList());

            return result;
        }
}
