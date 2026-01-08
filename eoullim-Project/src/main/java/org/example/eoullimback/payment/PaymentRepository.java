package org.example.eoullimback.payment;

import org.example.eoullimback.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
