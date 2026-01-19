package org.example.eoullimback.review;

import jakarta.validation.Valid;
import org.example.eoullimback.review.dto.AdminReviewListResponse;
import org.example.eoullimback.review.dto.ReviewListItemResponse;
import org.example.eoullimback.review.dto.ReviewRequest;
import org.example.eoullimback.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse.ListDTO> findByRoom(Long sessionUserId, Long placeId, Long roomId);

    void create(Long userId, Long roomId, ReviewRequest.@Valid CreateDTO request);

    Review findEntity(Long reviewId);

    void update(Long userId, Long reviewId, ReviewRequest.@Valid UpdateDTO request);

    void delete(Long userId, Long reviewId);

    boolean existsByPaymentId(Long paymentId);

    List<ReviewResponse.ListDTO> findLatestReviews(int limit);

    void adminDelete(Long adminUserId, Long reviewId);

    public List<ReviewListItemResponse> findList(Long userId, String code);

    List<AdminReviewListResponse> findAll(String keyword);
}
