package org.example.eoullimback.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.eoullimback.qna.Qna;
import org.example.eoullimback.user_auth.user.User;

public class CommentRequest {

    @Data
    public static class createDTO {

        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;

        @NotNull(message = "Q&A ID는 필수입니다.")
        private Long qnaId;

        public Comment toEntity(User user, Qna qna) {
            return Comment.builder()
                    .content(content)
                    .user(user)
                    .qna(qna)
                    .build();
        }
    }

}
