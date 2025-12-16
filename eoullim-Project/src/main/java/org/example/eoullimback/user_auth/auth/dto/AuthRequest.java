package org.example.eoullimback.user_auth.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthRequest {

    @Data
    @NoArgsConstructor
    public static class SignupRequest {
        private String loginId;
        private String password;
        private String name;
        private String email;
        private String phone;

        @Builder
        public SignupRequest(String loginId, String password, String name, String email, String phone) {
            this.loginId = loginId;
            this.password = password;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
    }

    @Data
    public static class LoginRequest {
        private String loginId;
        private String password;
    }
}
