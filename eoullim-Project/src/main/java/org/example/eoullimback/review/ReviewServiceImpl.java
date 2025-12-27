package org.example.eoullimback.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception409;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewResponse.ListDTO> findByRoom(Long roomId, Byte rating, String sort) {

        List<Review> reviews = (rating == null)
                ? reviewRepository.findByRoomId(roomId)
                : reviewRepository.findByRoomIdAndRating(roomId, rating);

        if("rating".equalsIgnoreCase(sort)) {
            reviews.sort(Comparator.comparing(Review::getRating).reversed());
        } else {
            reviews.sort(Comparator.comparing(Review::getRating).reversed());
        }

        return reviews.stream()
                .map(ReviewResponse.ListDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public void create(Long userId, Long roomId, ReviewRequest.@Valid CreateDTO request) {

        if (reviewRepository.existsByPaymentId(request.getPaymentId())) {
            throw new Exception409(ErrorCode.REVIEW_CONFLICT);
        }

        // 나중에 추가 예정 (User, Room, Payment)(더미데이터 확인 후)
        Review review = request.toEntity(null, null, null);
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public Review findEntity(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception404(ErrorCode.REVIEW_NOT_FOUND));
    }

    @Override
    public void update(Long userId, Long reviewId, ReviewRequest.@Valid UpdateDTO request) {

        Review review = findEntity(reviewId);

        if(!review.getUser().getId().equals(userId)) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        request.updateEntity(review);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long reviewId) {

        Review review = findEntity(reviewId);

        if (!review.getUser().getId().equals(userId)) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        reviewRepository.delete(review);
    }
}
