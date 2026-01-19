package org.example.eoullimback.qna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback.user_auth.user.User;

public class QnaRequest {

    @Data
    public static class CreateDTO {

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        public Qna toEntity(User user) {
            return new Qna(
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

        public void updateEntity(Qna qaa) {
            qaa.update(title, content);
        }
    }
}
