package org.example.eoullimback.qaa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback.user_auth.user.User;

public class QaaRequest {

    @Data
    public static class CreateDTO {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        public Qaa toEntity(User user) {
            return new Qaa(
                    title,
                    content,
                    user
            );
        }
    }

    @Data
    public static class UpdateDTO {
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        public void updateEntity(Qaa qaa) {
            qaa.update(title, content);
        }
    }
}
