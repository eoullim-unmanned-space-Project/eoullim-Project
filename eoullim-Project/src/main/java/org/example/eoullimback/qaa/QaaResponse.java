package org.example.eoullimback.qaa;

import lombok.Data;

import java.time.LocalDateTime;

public class QaaResponse {

    @Data
    public static class DetailDTO{

        private final Long id;
        private final String title;
        private final String content;
        private final Long userId;
        private final String name;
        private final Long viewCount;
        private final LocalDateTime createdAt;

        public DetailDTO(Qaa qaa) {
            this.id = qaa.getId();
            this.title = qaa.getTitle();
            this.content = qaa.getContent();
            this.userId = qaa.getUser() != null ? qaa.getUser().getId() : null;
            this.name = qaa.getUser() != null ? qaa.getUser().getName() : null;
            this.viewCount = qaa.getViewCount();
            this.createdAt = qaa.getCreatedAt();
        }
    }

    @Data
    public static class ListDTO{
        private Long id;
        private String title;
        private String name;
        private Long viewCount;
        private LocalDateTime createdAt;

        public ListDTO(Qaa qaa) {
            this.id = qaa.getId();
            this.title = qaa.getTitle();
            this.name = qaa.getUser() != null ? qaa.getUser().getName() : null;
            this.viewCount = qaa.getViewCount();
            this.createdAt = qaa.getCreatedAt();
        }
    }

    @Data
    public static class UpdateFormDTO{
        private Long id;
        private String title;
        private String content;
        private String name;
        private LocalDateTime updatedAt;

        public UpdateFormDTO(Qaa qaa) {
            this.id = qaa.getId();
            this.title = qaa.getTitle();
            this.content = qaa.getContent();
            this.name = qaa.getUser() != null ? qaa.getUser().getName() : null;
            this.updatedAt = qaa.getUpdatedAt();
        }
    }
}
