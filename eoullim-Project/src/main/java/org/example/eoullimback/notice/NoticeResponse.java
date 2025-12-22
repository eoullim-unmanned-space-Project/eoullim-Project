package org.example.eoullimback.notice;

import lombok.Data;

import java.time.LocalDateTime;

public class NoticeResponse {

    @Data
    public static class DetailDTO {

        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String name;
        private LocalDateTime createdAt;

        public DetailDTO(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.userId = notice.getUser() != null ? notice.getUser().getId() : null;
            this.name = notice.getUser() != null ? notice.getUser().getName() : null;
            this.createdAt = notice.getCreatedAt();
        }
    }

    @Data
    public static class ListDTO {
        private Long id;
        private String title;
        private String name;
        private LocalDateTime createdAt;

        public ListDTO(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.name = notice.getUser() != null ? notice.getUser().getName() : null;
            this.createdAt = notice.getCreatedAt();
        }
    }

    @Data
    public static class UpdateFormDTO {
        private Long id;
        private String title;
        private String content;
        private String name;
        private LocalDateTime updatedAt;

        public UpdateFormDTO(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.name = notice.getUser() != null ? notice.getUser().getName() : null;
            this.updatedAt = notice.getUpdatedAt();
        }
    }
}
