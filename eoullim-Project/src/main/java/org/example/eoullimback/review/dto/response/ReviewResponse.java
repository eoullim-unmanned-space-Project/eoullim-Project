package org.example.eoullimback.review.dto.response;

import org.example.eoullimback.review.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Byte rating,
        String content,
        Long userId,
        String userName,
        Long roomId,
        Long paymentId,
        LocalDateTime createAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getRoom().getId(),
                review.getPayment().getId(),
                review.getCreatedAt()
        );
    }
}
