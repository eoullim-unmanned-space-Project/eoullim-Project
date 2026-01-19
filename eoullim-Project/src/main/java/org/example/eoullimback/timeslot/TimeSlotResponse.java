package org.example.eoullimback.timeslot;

import lombok.Data;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;
import org.example.eoullimback._common.util.DateTimeUtil;

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
        private String startTime;
        private String endTime;
        private SlotStatus status;
        private LocalDateTime holdExpiredAt;

        public DetailDTO(TimeSlot timeSlot) {
            this.timeSlotId = timeSlot.getId();
            this.startTime = DateTimeUtil.toKstWithMinutesString(timeSlot.getStartTime());
            this.endTime = DateTimeUtil.toKstWithMinutesString(timeSlot.getEndTime());
            this.status = timeSlot.getStatus();
            this.holdExpiredAt = timeSlot.getHoldExpiredAt();
        }
    }
}
