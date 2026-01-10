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
        SELECT new org.example.eoullimback.review.ReviewablePaymentDTO(p.id)
                FROM Payment p
                WHERE p.user.id = :userId
                and p.id not in (
                        SELECT r.payment.id
                        FROM Review r
                        WHERE r.room.id = :roomId
                )
        """)
    List<ReviewablePaymentDTO> findReviewablePayment(@Param("userId") Long userId,  @Param("roomId") Long roomId);

    boolean existsByPaymentId(Long paymentId);
}
