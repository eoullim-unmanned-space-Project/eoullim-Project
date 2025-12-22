package org.example.eoullimback.timeslot;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;

import java.time.LocalDateTime;

public class TimeSlotRequest {

    @Data
    public static class CreateDTO {
        @NotBlank(message = "개시 시간은 필수입니다.")
        LocalDateTime startTime;

        @NotBlank(message = "종료 시간은 필수입니다.")
        LocalDateTime endTime;

        @NotBlank(message = "인원 수는 필수입니다.")
        int capacity;

        @NotBlank(message = "상태는 필수입니다.")
        RoomStatus status;

        public TimeSlot toEntity() {
            return TimeSlot.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .capacity(capacity)
                    .status(status)
                    .build();
        }
    }
}

