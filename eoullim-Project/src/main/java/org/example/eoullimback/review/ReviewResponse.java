package org.example.eoullimback.review;

import lombok.Data;

import java.time.LocalDateTime;

public class ReviewResponse {

    @Data
    public static class ListDTO {

        private Long id;
        private Byte rating;
        private String content;
        private Long userId;
        private String name;
        private Long roomId;
        private LocalDateTime createdAt;

        public ListDTO(Review review) {
            this.id = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.userId = review.getUser().getId();
            this.name = review.getUser().getName();
            this.roomId = review.getRoom().getId();
            this.createdAt = review.getCreatedAt();
        }
    }

    @Data
    public static class UpdateFormDTO {
        private Long id;
        private Byte rating;
        private String content;
        private LocalDateTime updatedAt;

        public UpdateFormDTO(Review review) {
            this.id = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.updatedAt = review.getUpdatedAt();
        }
    }
}
