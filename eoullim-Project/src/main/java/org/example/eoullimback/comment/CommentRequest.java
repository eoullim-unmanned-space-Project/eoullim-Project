package org.example.eoullimback.comment;

import lombok.Data;
import org.example.eoullimback.qaa.Qaa;
import org.example.eoullimback.user_auth.user.User;

public class CommentRequest {

    @Data
    public static class CommentSaveDto {
        private String comment;
        private User user;
        private Qaa qaa;

        public Comment toEntity(User user) {
            return new Comment(comment, user, qaa);
        }
    }

    @Data
    public static class CommentUpdateDto {
        private String comment;

        public void validate() {
            if(comment == null || comment.trim().isEmpty()) {
                throw new RuntimeException("제목은 필수 입니다.");
            }
        }
    }
}
