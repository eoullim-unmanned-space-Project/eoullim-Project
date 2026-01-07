package org.example.eoullimback.timeslot;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;
import org.example.eoullimback.room.Room;

import java.time.LocalDateTime;

public class TimeSlotRequest {

    @Data
    public static class CreateDTO {
        String slotMonth;
        LocalDateTime startTime;
        LocalDateTime endTime;
        int capacity;
        SlotStatus status;

        public void validate() {
            if (slotMonth == null || slotMonth.trim().isEmpty()) {
                throw new IllegalArgumentException("생성하는 날짜는 필수입니다.");
            }
            if (startTime == null) {
                throw new IllegalArgumentException("개시 시간은 필수입니다.");
            }
            if (endTime == null) {
                throw new IllegalArgumentException("종료 시간은 필수입니다.");
            }
            if (capacity >= 1) {
                throw new IllegalArgumentException("인원 수는 필수입니다.");
            }
            if (status == null) {
                throw new IllegalArgumentException("상태는 필수입니다.");
            }
        }

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

