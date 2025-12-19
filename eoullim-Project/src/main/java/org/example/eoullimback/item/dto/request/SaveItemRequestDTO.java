package org.example.eoullimback.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.example.eoullimback.timeslot.TimeSlot;

public record SaveItemRequestDTO(

        @NotBlank(message = "시간대는 필수 입니다.")
        TimeSlot timeSlot,

        @NotBlank(message = "가격은 필수 입니다.")
        int price
) {}
