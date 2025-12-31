package org.example.eoullimback.timeslot;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.room.Room;

import java.time.LocalDateTime;

public class TimeSlotRequest {

    @Data
    public static class CreateDTO {
        @NotBlank(message = "생성하는 날짜는 필수입니다.")
        String slotMonth;

        @NotBlank(message = "개시 시간은 필수입니다.")
        LocalDateTime startTime;

        @NotBlank(message = "종료 시간은 필수입니다.")
        LocalDateTime endTime;

        @NotBlank(message = "인원 수는 필수입니다.")
        int capacity;

        @NotBlank(message = "상태는 필수입니다.")
        RoomStatus status;

        public TimeSlot toEntity(Room room) {
            return TimeSlot.builder()
                    .room(room)
                    .slotMonth(slotMonth)
                    .startTime(startTime)
                    .endTime(endTime)
                    .capacity(capacity)
                    .status(status)
                    .build();
        }
    }
}

