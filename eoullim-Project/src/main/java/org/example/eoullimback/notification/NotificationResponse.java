package org.example.eoullimback.notification;

import lombok.Data;
import org.example.eoullimback._common.enums.notification.NotificationStatus;
import org.example.eoullimback._common.enums.notification.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {

    @Data
    public static class ListDTO {

        private Long id;
        private NotificationType type;
        private NotificationStatus status;
        private String message;
        private String qrCode;
        private Long userId;
        private Long paymentId;
        private LocalDateTime createdAt;
        private LocalDateTime sentAt;

        public ListDTO(Notification notification) {
            this.id = notification.getId();
            this.type = notification.getType();
            this.status = notification.getStatus();
            this.message = notification.getMessage();
            this.qrCode = notification.getQrCode();
            this.userId = notification.getUser().getId();
            this.paymentId = notification.getPayment().getId();
            this.createdAt = notification.getCreatedAt();
            this.sentAt = notification.getSentAt();
        }
    }
}
