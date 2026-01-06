package org.example.eoullimback.notification;

import lombok.Data;
import org.example.eoullimback._common.enums.notification.NotificationStatus;
import org.example.eoullimback._common.enums.notification.NotificationType;
import org.example.eoullimback.payment.Payment;

import java.time.LocalDateTime;

public class NotificationResponse {

    @Data
    public static class NotificationResponseDTO {

        private Long id;
        private NotificationType type;
        private NotificationStatus status;
        private String message;
        private String qrCode;
        private LocalDateTime createdAt;

        private String orderId;
        private String productName;
        private Long amount;

        public NotificationResponseDTO(Notification notification) {
            this.id = notification.getId();
            this.type = notification.getType();
            this.status = notification.getStatus();
            this.message = notification.getMessage();
            this.qrCode = notification.getQrCode();
            this.createdAt = notification.getCreatedAt();

            Payment payment = notification.getPayment();
            this.orderId = payment.getOrderId();
            this.productName = payment.getProductName();
            this.amount = payment.getAmount();
        }
    }
}
