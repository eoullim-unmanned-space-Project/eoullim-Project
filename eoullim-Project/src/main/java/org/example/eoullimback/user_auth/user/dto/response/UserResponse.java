package org.example.eoullimback.user_auth.user.dto.response;

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
}
