package org.example.eoullimback.review;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class ReviewResponse {

    @Data
    public static class ListDTO {

        private Long id;
        private Byte rating;
        private String content;
        private Long userId;
        private String name;
        private Long roomId;
        private Long placeId;
        private Long paymentId;
        private LocalDateTime createdAt;

        private List<Integer> stars;
        private List<Integer> emptyStars;

        private boolean mine;

        public ListDTO(Review review) {
            this(review, null);
        }

        public ListDTO(Review review, Long sessionUserId) {
            this.id = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.name = review.getUser().getName();
            this.roomId = review.getRoom().getId();
            this.placeId = review.getRoom().getPlace().getId();
            this.paymentId = review.getPayment().getId();
            this.createdAt = review.getCreatedAt();

            int r = review.getRating() != null ? review.getRating() : 0;
            this.stars = (r > 0) ? IntStream.range(0, r).boxed().toList() : List.of();
            this.emptyStars = IntStream.range(0, 5 - r).boxed().toList();

            this.mine = sessionUserId != null && review.getUser().getId().equals(sessionUserId);
        }
    }

    @Data
    public static class UpdateFormDTO {
        private Long id;
        private Byte rating;
        private String content;
        private LocalDateTime updatedAt;

        public boolean isRating1() { return rating != null && rating == 1; }
        public boolean isRating2() { return rating != null && rating == 2; }
        public boolean isRating3() { return rating != null && rating == 3; }
        public boolean isRating4() { return rating != null && rating == 4; }
        public boolean isRating5() { return rating != null && rating == 5; }

        public UpdateFormDTO(Review review) {
            this.id = review.getId();
            this.rating = review.getRating();
            this.content = review.getContent();
            this.updatedAt = review.getUpdatedAt();
        }

        public UpdateFormDTO(ReviewRequest.UpdateDTO req, Long reviewId) {
            this.id = reviewId;
            this.rating = req.getRating();
            this.content = req.getContent();
        }
    }
}
