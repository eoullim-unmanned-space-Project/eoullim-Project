package org.example.eoullimback.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
    SELECT r from Review r
    JOIN FETCH r.user
    JOIN FETCH r.room
    WHERE r.room.id = :roomId
""")
    List<Review> findByRoomId(@Param("roomId") long roomId);

    @Query("""
    SELECT r
    FROM Review r
    JOIN FETCH r.user
    JOIN FETCH r.room rm
    JOIN FETCH rm.place
    WHERE rm.id = :roomId
      AND rm.place.id = :placeId
""")
    List<Review> findByRoomAndPlace(@Param("roomId") Long roomId, @Param("placeId") Long placeId);

    @Query("""
    SELECT new org.example.eoullimback.review.ReviewablePaymentDTO(
        p.id,
        p.booking.room.id,
        p.booking.room.place.id
    )
    FROM Payment p
    WHERE p.user.id = :userId
      AND p.booking.room.id = :roomId
      AND p.id NOT IN (
          SELECT r.payment.id FROM Review r
      )
""")
    List<ReviewablePaymentDTO> findReviewablePayment(@Param("userId") Long userId,  @Param("roomId") Long roomId);

    boolean existsByPaymentId(Long paymentId);
}
