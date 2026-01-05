package org.example.eoullimback.notification;

import lombok.Data;
import org.example.eoullimback._common.enums.notification.NotificationStatus;
import org.example.eoullimback._common.enums.notification.NotificationType;

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

        public NotificationResponseDTO(Notification notification) {
            this.id = notification.getId();
            this.type = notification.getType();
            this.status = notification.getStatus();
            this.message = notification.getMessage();
            this.qrCode = notification.getQrCode();
            this.createdAt = notification.getCreatedAt();
        }
    }
}
