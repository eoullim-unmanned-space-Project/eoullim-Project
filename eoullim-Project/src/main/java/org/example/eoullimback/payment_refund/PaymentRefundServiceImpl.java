package org.example.eoullimback.payment_refund;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback.payment.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentRefundServiceImpl implements PaymentRefundService{

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
}
