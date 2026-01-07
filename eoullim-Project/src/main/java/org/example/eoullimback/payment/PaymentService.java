package org.example.eoullimback.payment;

public interface PaymentService {
    PaymentResponse.PrepareDTO prepare(Long id, String bookingCode);
    String complete(Long id, String impUid, String merchantUid);
    void cancel(String paymentKey);
}
