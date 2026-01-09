package org.example.eoullimback.payment_refund;

public interface PaymentRefundService {
     void createRefund(String paymentKey, String reason, Long id);
}
