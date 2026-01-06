package org.example.eoullimback.notification;

import jakarta.validation.constraints.NotNull;
import org.example.eoullimback.payment.Payment;

public interface NotificationService {
    void notifyPaymentSuccess(Payment payment);
    void notifyPaymentFailed(Payment payment, String reason);
    void notifyPaymentCancelled(Payment payment);
    void sendNotificationEmail(@NotNull(message = "알림 ID가 필요합니다") Long notificationId);
}
