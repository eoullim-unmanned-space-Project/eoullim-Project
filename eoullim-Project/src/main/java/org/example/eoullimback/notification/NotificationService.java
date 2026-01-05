package org.example.eoullimback.notification;

import org.example.eoullimback.payment.Payment;

public interface NotificationService {
    void notifyPaymentSuccess(Payment payment);
    void notifyPaymentFailed(Payment payment, String reason);
    void notifyPaymentCancelled(Payment payment);
}
