package org.example.eoullimback.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.geminichatbot.GeminiService;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment.PaymentRepository;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRepository;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PaymentRepository paymentRepository;
    private final GeminiService geminiService;

    @Override
    public List<ReviewResponse.ListDTO> findByRoom(Long sessionUserId, Long placeId, Long roomId) {
        List<Review> reviews = reviewRepository.findByRoomAndPlace(roomId, placeId);
        return reviews.stream()
                .map(r -> new ReviewResponse.ListDTO(r, sessionUserId))
                .toList();
    }

    @Override
    public boolean existsByPaymentId(Long paymentId) {
        return reviewRepository.existsByPaymentId(paymentId);
    }

    @Override
    public List<ReviewResponse.ListDTO> findLatestReviews(int limit) {
        var pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Review> reviews = reviewRepository.findAll(pageable).getContent();
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception409(ErrorCode.USER_NOT_FOUND));

        Payment payment = paymentRepository.findByIdWithBookingAndRoom(request.getPaymentId())
                .orElseThrow(() -> new Exception409(ErrorCode.PAYMENT_NOT_FOUND));

        String checkRequest = geminiService.checkReviewContent(request.getContent());

        if ("BAD".equals(checkRequest) || checkRequest.toUpperCase().contains("BAD")) {
            throw new Exception400(ErrorCode.BAD_CONTENT);
        }

        Room room = payment.getBooking().getRoom();

        Review review = Review.builder()
                .user(user)
                .room(room)
                .payment(payment)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public Review findEntity(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception404(ErrorCode.REVIEW_NOT_FOUND));
    }

    @Override
    @Transactional
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
