package org.example.eoullimback.payment;

import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPaymentKey(String paymentKey);

    boolean existsByBooking(Booking booking);

    Optional<Payment> findByPaymentKey(String merchantUid);

    Optional<Object> findByImpUid(String impUid);
}
