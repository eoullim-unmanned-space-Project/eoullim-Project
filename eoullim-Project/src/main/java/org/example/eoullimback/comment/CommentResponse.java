package org.example.eoullimback.comment;

import lombok.Data;

import java.time.LocalDateTime;

public class CommentResponse {

    @Data
    public static class CommentListDto {
        private Long id;
        private String comment;
        private String name;
        private LocalDateTime createAt;

        public CommentListDto(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            if(comment.getUser() != null) {
                this.name = comment.getUser().getName();
            }
            if(comment.getCreatedAt() != null) {
                this.createAt = getCreateAt();
            }
        }
    }

    @Data
    public static class UpdateFormDto {
        private Long id;
        private String comment;
        private String name;
        private LocalDateTime updateAt;

        public UpdateFormDto(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            this.name = comment.getUser().getName();
        }
    }
}
