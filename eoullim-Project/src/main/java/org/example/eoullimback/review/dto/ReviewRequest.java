package org.example.eoullimback.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.review.Review;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.user_auth.user.User;

public class ReviewRequest {

    @Data
    public static class CreateDTO {

        @NotNull(message = "결제 ID는 필수입니다.")
        private Long paymentId;

        @NotNull(message = "별점은 필수입니다.")
        @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5 이하여야 합니다.")
        private Byte rating;

        private String content;

        public Review toEntity(User user, Room room, Payment payment) {
            return Review.builder()
                    .rating(rating)
                    .content(content)
                    .user(user)
                    .room(room)
                    .payment(payment)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {

        @NotNull(message = "별점은 필수입니다.")
        @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5 이하여야 합니다.")
        private Byte rating;

        @NotNull
        private String content;

        public void updateEntity(Review review) {
            review.update(rating, content);
        }
    }
}
