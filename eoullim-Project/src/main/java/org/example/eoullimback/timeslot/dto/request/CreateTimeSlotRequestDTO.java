package org.example.eoullimback.timeslot.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.example.eoullimback._common.enums.place.PlaceStatus;

import java.time.LocalDateTime;

public record CreateTimeSlotRequestDTO(
        @NotBlank(message = "개시 시간은 필수입니다.")
        LocalDateTime startTime,

        @NotBlank(message = "종료 시간은 필수입니다.")
        LocalDateTime endTime,

        @NotBlank(message = "인원 수는 필수입니다.")
        int capacity,

        @NotBlank(message = "상태값은 필수 입니다.")
        PlaceStatus status
) {}

