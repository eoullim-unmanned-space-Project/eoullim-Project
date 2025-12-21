package org.example.eoullimback.user_auth.auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.example.eoullimback.user_auth.user.User;

public record AuthRequest() {

    @Builder
    public record SignupRequest(
            @NotBlank(message = "아이디는 필수입니다.")
            @Size(min = 6, max = 20, message = "아이디는 6자이상 20자 이하여야 합니다.")
            String loginId,

            @NotBlank(message = "비밀번호는 필수입니다.")
            @Size(min = 8, max = 20, message = "비밀번호는 8자이상 20자 이하여야 합니다.")
            String password,

            @NotBlank(message = "이름은 필수입니다.")
            String name,

            @NotBlank(message = "이메일은 필수입니다.")
            @Email(message = "@ 를 포함하여야 합니다.")
            String email,

            @NotBlank(message = "휴대폰 값은 필수입니다.")
            String phone
    ) {

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

    @Builder
    public record LoginRequest(
            @NotBlank(message = "아이디는 필수입니다.")
            String loginId,

            @NotBlank(message = "로그인은 필수입니다.")
            String password
    ) {}
}
