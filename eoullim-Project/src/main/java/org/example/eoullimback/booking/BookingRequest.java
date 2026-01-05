package org.example.eoullimback.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDate;

public class BookingRequest {

    @Data
    public static class createDTO {
        @NotNull(message = "스냅샷 가격은 공백일 수 없습니다.")
        Long itemSnapshotPrice;

        @Min(value = 1, message = "인원은 최소 1명 이상입니다.")
        int qty;

        @Min(value = 100, message = "가격은 최소 100원 이상입니다.")
        private Long amount;

        @NotNull(message = "예약한 달은 필수입니다.")
        private LocalDate bookingDate;

        @NotNull(message = "상태값은 필수입니다.")
        private BookingStatus status;

        public Booking toEntity(User user, Room room, TimeSlot timeSlot) {
            return Booking.builder()
                    .user(user)
                    .room(room)
                    .timeSlot(timeSlot)
                    .itemSnapshotPrice(itemSnapshotPrice)
                    .qty(qty)
                    .amount(amount)
                    .bookingDate(bookingDate)
                    .status(status)
                    .build();
        }
    }

}
