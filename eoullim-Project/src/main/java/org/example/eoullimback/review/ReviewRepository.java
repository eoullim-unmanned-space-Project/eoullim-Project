package org.example.eoullimback.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByPaymentId(long paymentId);

    List<Review> findByRoomId(long roomId);

    List<Review> findByRoomIdAndRating(long roomId, Byte rating);
}
