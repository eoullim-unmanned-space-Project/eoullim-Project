package org.example.eoullimback.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingRequest {

    @Data
    public static class CalculateAmountDTO {
        private Long roomId;
        private List<Long> timeSlotIds;
    }

    @Data
    public static class createDTO {
        private Long roomId;

        private List<Long> timeSlotIds;

        @Min(value = 1, message = "인원은 최소 1명 이상입니다.")
        private int qty;

        @Min(value = 100, message = "가격은 최소 100원 이상입니다.")
        private Long totalAmount;

        private LocalDate bookingDate;

        public Booking toEntity(User user, Room room, TimeSlot timeSlot, String bookingCode, Long perSlotAmount, LocalDate bookingDate) {
            return Booking.builder()
                    .user(user)
                    .room(room)
                    .timeSlot(timeSlot)
                    .bookingCode(bookingCode)
                    .itemSnapshotPrice(totalAmount)
                    .qty(qty)
                    .amount(perSlotAmount)
                    .bookingDate(bookingDate)
                    .build();
        }
    }
}
