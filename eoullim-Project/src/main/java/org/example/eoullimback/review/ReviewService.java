package org.example.eoullimback.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.review.dto.request.ReviewSaveRequest;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRepository;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
//    private final PaymentRepository paymentRepository;

//    public Long saveReview(ReviewSaveRequest request, User user) {
//
//        Room room = roomRepository.findById(request.roomId())
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 방입니다."));
//
//      Payment payment = paymentRepository
//
//        Review review = Review.builder()
//                .rating(request.rating())
//                .content(request.content())
//                .user(user)
//                .room(room)
//              .payment(payment)
//                .build();
//
//        reviewRepository.save(review);
//        return review.getId();
//    }
}
