package org.example.eoullimback.booking;

import lombok.Data;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.util.DateTimeUtil;
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
        // 사용자 이름
        private String username;
        // 장소 이름
        private String placeName;
        // 장소 주소
        private String address;
        // 장소 위도
        private BigDecimal latitude;
        // 장소 경도
        private BigDecimal longitude;
       // 룸 아이디
        private Long roomId;
        // 룸 이름
        private String roomName;
        // 룸 설명 
        private String content;
        // 총 가격
        private Long amount;
        // 예약 날짜
        private LocalDate bookingDate;
        // 예약 인원
        private int qty;
        // 예약 코드
        private String bookingCode;
        // 예약 상태
        private BookingStatus status;
        private List<TimeSlotResponse.DetailDTO> timeSlots;

        public DetailDTO(List<Booking> bookings) {
            Booking grouping = bookings.get(0);

            this.username = grouping.getUser().getName();
            this.placeName = grouping.getRoom().getPlace().getName();
            this.address = grouping.getRoom().getPlace().getAddress();
            this.latitude = grouping.getRoom().getPlace().getLatitude();
            this.longitude = grouping.getRoom().getPlace().getLongitude();
            this.roomId = grouping.getRoom().getId();
            this.roomName = grouping.getRoom().getName();
            this.content = grouping.getRoom().getContent();
            this.amount = grouping.getItemSnapshotPrice();
            this.bookingDate = grouping.getBookingDate();
            this.qty = grouping.getQty();
            this.bookingCode = grouping.getBookingCode();
            this.status = grouping.getStatus();
            // 3개의 타임슬롯을 List로 묶어준다
            this.timeSlots = bookings.stream()
                    .map(booking -> new TimeSlotResponse.DetailDTO(booking.getTimeSlot())).toList();
        }
    }
}
