package org.example.eoullimback.review;

import jakarta.validation.Valid;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse.ListDTO> findByRoom(Long roomId, Byte rating, String sort);

    void create(Long userId, Long roomId, ReviewRequest.@Valid CreateDTO request);

    Review findEntity(Long reviewId);

    void update(Long userId, Long reviewId, ReviewRequest.@Valid UpdateDTO request);

    void delete(Long userId, Long reviewId);
}
