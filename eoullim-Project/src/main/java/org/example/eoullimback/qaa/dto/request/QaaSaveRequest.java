package org.example.eoullimback.qaa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.eoullimback.qaa.Qaa;
import org.example.eoullimback.user_auth.user.User;

public record QaaSaveRequest(

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
        String title,

        @NotBlank(message = "내용은 필수 입니다.")
        String content,

        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId
) {
    public Qaa toEntity(User user) {
        return new Qaa(
                title,
                content,
                0L,
                user
        );
    }
}
