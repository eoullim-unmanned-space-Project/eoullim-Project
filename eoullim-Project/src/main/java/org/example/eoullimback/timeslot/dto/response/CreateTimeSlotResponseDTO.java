package org.example.eoullimback.timeslot.dto.response;

import org.example.eoullimback.timeslot.TimeSlot;

import java.time.LocalDateTime;

public record CreateTimeSlotResponseDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
        int capacity
) {
    public static CreateTimeSlotResponseDTO form(TimeSlot timeSlot) {
        return new CreateTimeSlotResponseDTO(
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getCapacity()
        );
    }
}
