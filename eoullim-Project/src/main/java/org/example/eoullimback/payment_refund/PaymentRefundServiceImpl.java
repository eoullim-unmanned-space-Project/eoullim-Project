package org.example.eoullimback.payment_refund;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentRefundServiceImpl implements PaymentRefundService{

    @Value("${portone.imp-key}")
    private String impKey;

    @Value("${portone.imp-secret-key}")
    private String impSecret;

    private final PaymentRefundRepository paymentRefundRepository;

    @Override
    @Transactional
    public void createRefund(String paymentKey, String reason, Long userId) {

        Payment payment = paymentRefundRepository.findByUserIdAndPaymentKey(userId, paymentKey)
                .orElseThrow(() -> new Exception400(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new Exception400(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        if (paymentRefundRepository.existsByPaymentAndStatus(payment, RefundStatus.REQUESTED)) {
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

        return refundEntities.stream()
                .map(PaymentRefundResponse.AdminListDTO::new)
                .toList();
    }

    @Override
    public PaymentRefundResponse.AdminDetailDTO detail(Long id) {

        PaymentRefund refundEntity = paymentRefundRepository.findByIdPayment(id)
                .orElseThrow(() -> new Exception404(ErrorCode.PAYMENT_REFUND_NOT_FOUND));

        return new PaymentRefundResponse.AdminDetailDTO(refundEntity);
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

        refundEntity.markReject(reason);
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

}
