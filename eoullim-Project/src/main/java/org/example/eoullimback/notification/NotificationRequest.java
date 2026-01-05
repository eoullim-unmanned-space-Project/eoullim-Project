package org.example.eoullimback.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.eoullimback._common.enums.notification.NotificationStatus;
import org.example.eoullimback._common.enums.notification.NotificationType;

import java.time.LocalDateTime;

public class NotificationRequest {

    // 알림 요청 생성
    @Data
    public static class CreateDTO {

        @NotNull
        private NotificationType type;
        @NotNull
        private NotificationStatus status;
        @NotNull
        private String message;
        @NotNull
        private String qrCode;

        private LocalDateTime createdAt;
        private LocalDateTime sentAt;

        public Notification toEntity() {
            return Notification.builder()
                    .type(type)
                    .status(status)
                    .message(message)
                    .qrCode(qrCode)
                    .createdAt(createdAt)
                    .sentAt(sentAt)
                    .build();
        }
    }

}
