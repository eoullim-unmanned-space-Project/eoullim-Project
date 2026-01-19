package org.example.eoullimback.booking;

import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.user u
            JOIN FETCH b.room r
            JOIN FETCH b.timeSlot t
            JOIN FETCH r.place p
            WHERE b.bookingCode = :bookingCode
            AND u.id = :userId
            """)
    Optional<List<Booking>> findDetailByBookingCodeAndUser(@Param("userId") Long id, @Param("bookingCode") String bookingCode);

    @Query("SELECT b FROM Booking b JOIN FETCH b.user WHERE b.bookingCode = :bookingCode")
    Optional<List<Booking>> findAllByBookingCode(String bookingCode);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.status = :status
            AND b.bookingTime <= :limitTime
            """)
    List<Booking> findAllByStatusAndCreatedAtBefore(@Param("status") BookingStatus bookingStatus, @Param("limitTime") LocalDateTime limitTime);

    @Query("""
            SELECT b FROM Booking b
            LEFT JOIN FETCH b.user u
            LEFT JOIN FETCH b.room r
            LEFT JOIN FETCH b.timeSlot t
            LEFT JOIN FETCH r.place p
            WHERE (:bookingCode IS NULL OR b.bookingCode = :bookingCode)
            AND (:status IS NULL OR b.status = :status)
            AND u.id = :userId
            ORDER BY b.id DESC
            """)
    List<Booking> findAllByUserIdAndBookingCodeAndStatus(@Param("userId") Long id, @Param("bookingCode") String bookingCode, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.timeSlot WHERE b.bookingCode = :bookingCode ORDER BY b.timeSlot.startTime DESC")
    Optional<List<Booking>> findByBookingCodeWithTimeSlots(@Param("bookingCode") String bookingCode);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.timeSlot ts
            WHERE b.bookingCode = :bookingCode
            ORDER BY ts.startTime
            """)
    Optional<List<Booking>> findAllByBookingCodeWithTimeSlot(@Param("bookingCode") String bookingCode);

    @Query("""
               SELECT b From Booking b
               JOIN FETCH b.timeSlot
            """)
    List<Booking> findAllWithTimeSlot();

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.user u
            WHERE b.bookingCode = :bookingCode
            AND u.id = :userId
            """)
    List<Booking> findAllByBookingCodeWithUser(@Param("bookingCode") String bookingCode, @Param("userId") Long userId);

    @Query("""
            SELECT COUNT(b) FROM Booking b
            WHERE b.status = 'CONFIRMED'
            AND b.bookingTime > :today
            """)
    Long countTodayBookings(@Param("today") LocalDateTime today);

    @Query("""
            SELECT COUNT(b) FROM Booking b
            WHERE b.status = 'CONFIRMED'
            AND b.bookingTime <= :yesterday
            AND b.bookingTime > :today
            """)
    Long countYesterdayBookings(@Param("today") LocalDateTime today, @Param("yesterday") LocalDateTime yesterday);
}
