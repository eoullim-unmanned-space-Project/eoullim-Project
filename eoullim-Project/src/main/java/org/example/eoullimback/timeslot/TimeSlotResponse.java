package org.example.eoullimback.timeslot;

import jakarta.persistence.Column;
import lombok.Data;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;

import java.time.LocalDateTime;

public class TimeSlotResponse {

    @Data
    public static class ListDTO {
        LocalDateTime startTime;
        LocalDateTime endTime;
        int capacity;

        public ListDTO(TimeSlot timeSlot) {
            this.startTime = timeSlot.getStartTime();
            this.endTime = timeSlot.getEndTime();
            this.capacity = timeSlot.getCapacity();
        }
    }


    @Data
    public static class DetailDTO {
        private Long timeSlotId;

        private LocalDateTime startTime;

        private LocalDateTime endTime;
        private SlotStatus status;

        public DetailDTO(TimeSlot timeSlot) {
            this.timeSlotId = timeSlot.getId();
            this.startTime = timeSlot.getStartTime();
            this.endTime = timeSlot.getEndTime();
            this.status = timeSlot.getStatus();
        }
    }
}
