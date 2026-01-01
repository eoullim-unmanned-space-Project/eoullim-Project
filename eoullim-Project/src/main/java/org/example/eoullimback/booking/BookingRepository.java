package org.example.eoullimback.booking;

import org.example.eoullimback.room.Room;
import org.example.eoullimback.timeslot.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByRoomAndBookingDateAndTimeSlotIn(Room roomEntity, LocalDate now, List<TimeSlot> timeslotEntities);

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
}
