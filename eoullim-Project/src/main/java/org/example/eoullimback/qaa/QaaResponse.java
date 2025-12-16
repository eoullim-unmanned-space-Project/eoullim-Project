package org.example.eoullimback.qaa;

import lombok.Data;
import org.example.eoullimback.comment.CommentResponse;

import java.time.LocalDateTime;
import java.util.List;

public class QaaResponse {

    @Data
    public static class QaaListDto {
        private Long id;
        private String title;
        private String name;
        private Long viewCount;
        private LocalDateTime createAt;

        public QaaListDto(Qaa qaa) {
            this.id = qaa.getId();
            this.title = qaa.getTitle();
            if(qaa.getUser() != null) {
                this.name = qaa.getUser().getName();
            }
            this.viewCount = qaa.getViewCount();
            if(qaa.getCreatedAt() != null) {
                this.createAt = getCreateAt();
            }
        }
    }

    @Data
    public static class QaaDetailDto {
        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String name;
        private Long viewCount;
        private String createAt;

        private List<CommentResponse.CommentListDto> commentList;

        public QaaDetailDto(Qaa qaa) {
            this.id = qaa.getId();
            this.title = qaa.getTitle();
            this.content = qaa.getContent();
            if(qaa.getUser() != null) {
                this.userId = qaa.getUser().getId();
                this.name = qaa.getUser().getName();
            }
            this.viewCount = qaa.getViewCount();
            if(qaa.getCreatedAt() != null) {
                this.createAt = getCreateAt();
            }
            this.commentList = qaa.getCommentList().stream()
                    .map(CommentResponse.CommentListDto::new)
                    .toList();
        }
    }

    @Data
    public static class QaaUpdateFormDto {
        private Long id;
        private String title;
        private String content;
        private String name;
        private LocalDateTime updateAt;

        public QaaUpdateFormDto(Qaa qaa) {
            this.id = qaa.getId();
            this.title = qaa.getTitle();
            this.content = qaa.getContent();
            this.name = qaa.getUser().getName();
            this.updateAt = qaa.getUpdatedAt();
        }
    }
}
