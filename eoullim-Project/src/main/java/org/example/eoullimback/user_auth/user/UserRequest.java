package org.example.eoullimback.user_auth.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class UpdateProfileRequest {
        private String name;
        private String email;
        private String phone;

        public UpdateProfileRequest(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("이름은 필수입니다.");
            }
            if (email == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("이메일은 필수입니다.");
            }
            if (!email.contains("@")) { // (email.contains("@") == false)
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
            }
        }
    }

    public static class UpdateRoleRequest {
        private String role;

        public UpdateRoleRequest(String role) {
            this.role = role;
        }
    }
}
