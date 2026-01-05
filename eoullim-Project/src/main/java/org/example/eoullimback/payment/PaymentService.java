package org.example.eoullimback.payment;

public interface PaymentService {
    PaymentResponse.PrepareDTO prepare(Long id, String bookingCode);
    void complete(Long id, String impUid, String merchantUid);
}
