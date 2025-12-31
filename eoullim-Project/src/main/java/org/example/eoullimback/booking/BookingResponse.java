package org.example.eoullimback.booking;

import lombok.Data;

public class BookingResponse {

    @Data
    public static class CalculateAmountDTO {
        private Long amount;

    public CalculateAmountDTO(Long amount) {
        this.amount = amount;
    }
}
}
