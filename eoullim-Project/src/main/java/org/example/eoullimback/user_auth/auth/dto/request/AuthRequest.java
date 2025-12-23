package org.example.eoullimback.user_auth.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback.user_auth.user.User;

public class AuthRequest {

    @Data
    public static class SignupRequestDTO {
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 6, max = 20, message = "아이디는 6자이상 20자 이하여야 합니다.")
        private String loginId;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자이상 20자 이하여야 합니다.")
        private String password;

        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "@ 를 포함하여야 합니다.")
        private String email;

        @NotBlank(message = "휴대폰 값은 필수입니다.")
        private String phone;

       public User toEntity() {
           return User.builder()
                   .loginId(loginId)
                   .password(password)
                   .name(name)
                   .email(email)
                   .phone(phone)
                   .build();
       }
    }

    @Data
    public static class LoginRequestDTO {
        @NotBlank(message = "아이디는 필수입니다.")
        private String loginId;

        @NotBlank(message = "로그인은 필수입니다.")
        private String password;
    }
}
