package org.example.eoullimback.timeslot;

import lombok.Data;

import java.time.LocalDateTime;

public class TimeSlotResponse {

    @Data
    public static class ListDTO {
        LocalDateTime startTime;
        LocalDateTime endTime;
        int capacity;

        public ListDTO(TimeSlot timeSlot) {
            this. startTime = timeSlot.getStartTime();
            this. endTime = timeSlot.getEndTime();
            this. capacity = timeSlot.getCapacity();
        }
    }
}
