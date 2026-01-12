package org.example.eoullimback.review;

import jakarta.validation.Valid;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse.ListDTO> findByRoom(Long sessionUserId, Long placeId, Long roomId);

    void create(Long userId, Long roomId, ReviewRequest.@Valid CreateDTO request);

    Review findEntity(Long reviewId);

    void update(Long userId, Long reviewId, ReviewRequest.@Valid UpdateDTO request);

    void delete(Long userId, Long reviewId);

    boolean existsByPaymentId(Long paymentId);

    List<ReviewablePaymentDTO> findReviewablePayments(Long userId, Long roomId);

    List<ReviewResponse.ListDTO> findLatestReviews(int limit);
}
