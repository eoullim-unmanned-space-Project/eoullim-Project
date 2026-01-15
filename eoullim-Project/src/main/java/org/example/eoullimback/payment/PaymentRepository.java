package org.example.eoullimback.payment;

import jakarta.validation.constraints.NotNull;
import org.example.eoullimback.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPaymentKey(String paymentKey);

    boolean existsByBooking(Booking booking);

    Optional<Payment> findByPaymentKey(String merchantUid);

    Optional<Object> findByImpUid(String impUid);

    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.booking
            JOIN FETCH p.user
            WHERE p.user.id = :userId
            AND  p.booking.bookingCode = :bookingCode
            """)
    Optional<Payment> findByUserIdWithPayment(@Param("userId") Long userId, @Param("bookingCode")String bookingCode);

    @Query("""
           SELECT p FROM Payment p
           JOIN FETCH p.booking b
           WHERE b IN :bookingEntities
           """)
    List<Payment> findAllPaymentInBookings(List<Booking> bookingEntities);

    @Query("""
            SELECT p FROM Payment p
                        JOIN FETCH p.booking b
                                    JOIN FETCH b.room r
                                                WHERE p.id = :paymentId
            """)
    Optional<Payment> findByIdWithBookingAndRoom(@NotNull(message = "결제 ID는 필수입니다.") Long paymentId);

    @Query("""
        SELECT p FROM Payment p
        JOIN FETCH p.user u
        JOIN FETCH p.booking b
        JOIN FETCH b.room r
        JOIN FETCH r.place pe
        ORDER BY p.id DESC
        """)
    List<Payment> findAllPayment();


    @Query("""
        SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
        WHERE p.status = 'COMPLETED'
        AND p.createdAt >= CURRENT_DATE
       """)
    Long getTodaySales();


    @Query("""
        SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
        WHERE p.status = 'COMPLETED'
        AND p.createdAt >= :startDay
        AND p.createdAt < :endDay
    """)
    Long getYesterdaySales(@Param("startDay")LocalDateTime startDay, @Param("endDay")LocalDateTime endDay);



}
