package org.example.eoullimback.review.dto;

import org.example.eoullimback.review.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public record ReviewView(
        Long id,
        String username,
        String content,
        Integer rating,
        List<Boolean> stars,
        LocalDateTime createdAt
) {
    public static ReviewView from(Review review) {
        return new ReviewView(
                review.getId(),
                review.getUser().getName(),
                review.getContent(),
                review.getRating(),
                IntStream.rangeClosed(1, 5)
                        .mapToObj(i -> i <= review.getRating())
                        .toList(),
                review.getCreatedAt()
        );
    }
}
