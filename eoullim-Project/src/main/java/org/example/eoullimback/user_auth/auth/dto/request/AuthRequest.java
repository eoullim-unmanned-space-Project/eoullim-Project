package org.example.eoullimback.user_auth.auth.dto.request;

import lombok.Data;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.Role;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback.user_auth.user.User;

public class AuthRequest {

    @Data
    public static class SignupRequestDTO {

        private String loginId;
        private String password;
        private String name;
        private String email;
        private String phone;

        private String verificationCode;

       public User toEntity() {
           User user = User.builder()
                   .loginId(this.loginId)
                   .password(this.password)
                   .name(this.name)
                   .email(this.email)
                   .phone(this.phone)
                   .profileImage("/img/user/default.png")
                   .build();

           user.addRole(Role.USER);
           return user;
       }

       public void validate() {
           if (loginId == null || loginId.trim().isEmpty()) {
               throw new Exception400(ErrorCode.LOGIN_ID_REQUIRED);
           }
           if (loginId.length() < 6 || loginId.length() > 20) {
               throw new Exception400(ErrorCode.LOGIN_ID_LENGTH);
           }
           if (password == null || password.trim().isEmpty()) {
               throw new Exception400(ErrorCode.PASSWORD_REQUIRED);
           }
           if (password.length() < 8 ||  password.length() > 20) {
               throw new Exception400(ErrorCode.PASSWORD_POLITY);
           }
           if (name == null || name.trim().isEmpty()) {
               throw new Exception400(ErrorCode.NAME_REQUIRED);
           }
           if (email == null || email.isEmpty()) {
               throw new Exception400(ErrorCode.MISSING_EMAIL);
           }
           if (!email.contains("@")) {
               throw new Exception400(ErrorCode.INVALID_EMAIL_FORMAT);
           }
           if (phone == null || phone.trim().isEmpty()) {
               throw new Exception400(ErrorCode.PHONE_REQUIRED);
           }

           if (verificationCode == null || verificationCode.trim().isEmpty()) {
               throw new Exception400(ErrorCode.MISSING_VERIFICATION_CODE);
           }
       }
    }

    @Data
    public static class LoginRequestDTO {

        private String loginId;
        private String password;

        public void validate() {
            if (loginId == null || loginId.trim().isEmpty()) {
                throw new Exception400(ErrorCode.LOGIN_ID_REQUIRED);
            }
            if (password == null || password.trim().isEmpty()) {
                throw new Exception400(ErrorCode.PASSWORD_REQUIRED);
            }
        }
    }

    @Data
    public static class FindLoginIdRequestDTO {
        private String name;
        private String email;

        public void validate() {
            if (name == null || name.isEmpty()) {
                throw new Exception400(ErrorCode.NAME_REQUIRED);
            }

            if (email == null || email.isEmpty()) {
                throw new Exception400(ErrorCode.MISSING_EMAIL);
            }
        }
    }

    @Data
    public static class ResetPasswordRequestDTO {

        private String loginId;
        private String email;
        public void validate() {
            if (loginId == null || loginId.trim().isEmpty()) {
                throw new Exception400(ErrorCode.LOGIN_ID_REQUIRED);
            }

            if (email == null || email.isEmpty()) {
                throw new Exception400(ErrorCode.MISSING_EMAIL);
            }
        }
    }

    @Data
    public static class ResetPasswordConfirmDTO {

        private String token;
        private String newPassword;

        public void validate() {
            if (token == null || token.trim().isBlank()) {
                throw new Exception400(ErrorCode.INVALID_INPUT);
            }
            if (newPassword == null || newPassword.trim().isBlank()) {
                throw new Exception400(ErrorCode.PASSWORD_REQUIRED);
            }
        }
    }

    @Data
    public static class UserSuspendRequestDTO {

        private String reason;

        public void validate() {
            if (reason == null || reason.trim().isBlank()) {
                throw new Exception400(ErrorCode.INVALID_INPUT);
            }
        }
    }
}
