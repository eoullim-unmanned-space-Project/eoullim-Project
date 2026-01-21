package org.example.eoullimback.booking;

import lombok.Data;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.timeslot.TimeSlotResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingResponse {

    @Data
    public static class CalculateAmountDTO {
        private Long amount;

    public CalculateAmountDTO(Long amount) {
        this.amount = amount;
    }
    }

    @Data
    public static class DetailDTO {
        private String username;
        private String placeName;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Long roomId;
        private String roomName;
        private String roomImage;
        private String content;
        private Long amount;
        private LocalDate bookingDate;
        private int qty;
        private String bookingCode;
        private BookingStatus status;
        private String profileImage;
        private List<TimeSlotResponse.DetailDTO> timeSlots;

        public DetailDTO(List<Booking> bookings) {
            Booking grouping = bookings.get(0);

            this.username = grouping.getUser().getName();
            this.placeName = grouping.getRoom().getPlace().getName();
            this.address = grouping.getRoom().getPlace().getAddress();
            this.latitude = grouping.getRoom().getPlace().getLatitude();
            this.longitude = grouping.getRoom().getPlace().getLongitude();
            this.profileImage = grouping.getRoom().getPlace().getProfileImage();
            this.roomId = grouping.getRoom().getId();
            this.roomName = grouping.getRoom().getName();
            this.roomImage = grouping.getRoom().getRoomImage();
            this.content = grouping.getRoom().getContent();
            this.amount = grouping.getItemSnapshotPrice();
            this.bookingDate = grouping.getBookingDate();
            this.qty = grouping.getQty();
            this.bookingCode = grouping.getBookingCode();
            this.status = grouping.getStatus();
            this.timeSlots = bookings.stream()
                    .map(booking -> new TimeSlotResponse.DetailDTO(booking.getTimeSlot())).toList();
        }
    }

    @Data
    public static class CountDTO {
        Long todayCount;
        Long yesterdayCount;

        CountDTO(Long todayCount, Long yesterdayCount) {
            this.todayCount = todayCount;
            this.yesterdayCount = yesterdayCount;
        }
    }
}
