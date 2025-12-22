package org.example.eoullimback.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.eoullimback.timeslot.TimeSlot;

public class ItemRequest {

    @Data
    public static class CreateDTO {
        @NotBlank(message = "시간대는 필수 입니다.")
        private TimeSlot timeSlot;

        @NotBlank(message = "가격은 필수 입니다.")
        private int price;

        public Item toEntity () {
            return Item.builder()
                    .timeSlot(timeSlot)
                    .price(price)
                    .build();
        }
    }
}