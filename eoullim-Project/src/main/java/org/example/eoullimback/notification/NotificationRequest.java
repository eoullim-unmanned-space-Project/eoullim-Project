package org.example.eoullimback.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class NotificationRequest {

    @Data
    public static class NotificationDTO {

        @NotNull(message = "알림 ID가 필요합니다")
        private Long notificationId;
    }
}
