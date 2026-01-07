package org.example.eoullimback.notification;

import org.example.eoullimback.payment.Payment;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse.NotificationResponseDTO> notificationList(Long id);
    void notifyPaymentSuccess(Payment payment);
    void notifyPaymentFailed(Payment payment, String reason);
    void notifyPaymentCancelled(Payment payment);
}
