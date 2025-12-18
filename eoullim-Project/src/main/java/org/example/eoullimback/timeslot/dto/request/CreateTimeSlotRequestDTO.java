package org.example.eoullimback.timeslot.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CreateTimeSlotRequestDTO(
        @NotBlank(message = "개시 시간은 필수입니다.")
        LocalDateTime startTime,

        @NotBlank(message = "종료 시간은 필수입니다.")
        LocalDateTime endTime,

        @NotBlank(message = "인원 수는 필수입니다.")
        int capacity

) {}

