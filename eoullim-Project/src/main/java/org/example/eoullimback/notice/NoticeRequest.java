package org.example.eoullimback.notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.eoullimback.user_auth.user.User;

public class NoticeRequest {

    @Data
    public static class CreateDTO {

        @NotBlank(message = "공지사항 제목은 필수입니다.")
        @Size(max = 50, message = "공지사항 제목은 50자 이내여야 합니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;

        public Notice toEntity(User user) {
            return Notice.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {

        @NotBlank(message = "공지사항 제목은 필수입니다.")
        @Size(max = 50, message = "공지사항 제목은 50자 이내여야 합니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        private String content;
    }
}
