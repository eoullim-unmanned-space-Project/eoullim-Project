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

    boolean existsByPaymentId(Long paymentId);

    @Query("""
        SELECT new org.example.eoullimback.review.ReviewListItemDTO(
            p.id,
            p.orderId,
            p.booking.room.id,
            p.booking.room.place.id,
            p.booking.room.name,
            p.amount,
            p.status,
            CASE WHEN r.id IS NULL THEN false ELSE true END,
            r.id,
            r.rating,
            r.content
        )
        FROM Payment p
        LEFT JOIN Review r ON r.payment.id = p.id
        WHERE p.user.id = :userId
          AND (:code = '' OR p.orderId LIKE %:code%)
        ORDER BY p.requestedAt DESC
        """)
    List<ReviewListItemDTO> findMyReviewList(@Param("userId") Long userId,
                                             @Param("code") String code);
}
