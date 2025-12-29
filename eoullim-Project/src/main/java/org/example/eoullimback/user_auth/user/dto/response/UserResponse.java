package org.example.eoullimback.user_auth.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDateTime;

public record UserResponse() {

    public record Detail(
            String name,
            String email,
            String phone,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Detail from(User user) {
            return new Detail(
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        }
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OAuthToken {
        private String tokenType;
        private String accessToken;
        private String expiresIn;
        private String refreshToken;
        private String refreshTokenExpiresIn;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoProfile {
        private Long id;
        private String connectedAt;
        private Properties properties;
    }

    @Data
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {
        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }
}
