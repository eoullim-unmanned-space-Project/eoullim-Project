package org.example.eoullimback.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.eoullimback.qaa.Qaa;
import org.example.eoullimback.user_auth.user.User;

public class CommentRequest {

    @Data
    public static class createDTO {

        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;

        @NotNull(message = "Q&A ID는 필수입니다.")
        private Long qaaId;

        public Comment toEntity(User user, Qaa qaa) {
            return Comment.builder()
                    .content(content)
                    .user(user)
                    .qaa(qaa)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {

        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;

        public void updateEntity(Comment comment) {
            comment.update(content);
        }
    }

}
