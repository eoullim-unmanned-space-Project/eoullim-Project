package org.example.eoullimback.timeslot.dto.response;

import org.example.eoullimback._common.enums.place.PlaceStatus;
import org.example.eoullimback.timeslot.TimeSlot;

import java.time.LocalDateTime;

public record CreateTimeSlotResponseDTO(
        LocalDateTime startTime,
        LocalDateTime endTime,
        int capacity,
        PlaceStatus status
) {
    public static CreateTimeSlotResponseDTO form(TimeSlot timeSlot) {
        return new CreateTimeSlotResponseDTO(
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getCapacity(),
                timeSlot.getStatus()
        );
    }
}
