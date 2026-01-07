package org.example.eoullimback.timeslot;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endTime;

        public DetailDTO(TimeSlot timeSlot) {
            this.startTime = timeSlot.getStartTime();
            this.endTime = timeSlot.getEndTime();
        }
    }
}
