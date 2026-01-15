package org.example.eoullimback.payment_refund;

import java.util.List;

public interface PaymentRefundService {
    void createRefund(String paymentKey, String reason, Long id);
    List<PaymentRefundResponse.AdminListDTO> getRefundList();
    PaymentRefundResponse.AdminDetailDTO detail(Long id);
    void rejection(Long id, String reason);
    void approve(Long id);
    Long countPaymentsInRefundRequested();
}
