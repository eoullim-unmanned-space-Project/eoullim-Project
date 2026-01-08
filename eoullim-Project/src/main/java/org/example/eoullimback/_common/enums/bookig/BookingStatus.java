package org.example.eoullimback._common.enums.bookig;

import lombok.Getter;

@Getter
public enum BookingStatus {
    PENDING("예약대기"),
    CONFIRMED("예약완료"),
    CANCELED("취소완료"),
    REFUNDED("환불완료");

    private final String formatter;

    BookingStatus(String formatter) {
        this.formatter = formatter;
    }
}
