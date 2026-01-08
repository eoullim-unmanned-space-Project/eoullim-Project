package org.example.eoullimback.room;

import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.timeslot.TimeSlotResponse;

import java.util.List;

public class RoomResponse {

    @Data
    public static class ListDTO {
        private Long roomId;
        private String name;
        private String roomImagePath;

        public ListDTO(Room room) {
            this.roomId = room.getId();
            this.name = room.getName();
            this.roomImagePath = "/images/" + room.getRoomImage();
        }
    }

    @Data
    public static class DetailDTO {
        private Long roomId;
        private String name;
        private String content;
        private int maxCapacity;
        private int defaultPrice;
        private RoomStatus status;
        private String roomImagePath;
        private List<TimeSlotResponse.DetailDTO> timeSlots;

        public DetailDTO(Room room) {
            this.roomId = room.getId();
            this.name = room.getName();
            this.content = room.getContent();
            this.maxCapacity = room.getMaxCapacity();
            this.defaultPrice = room.getDefaultPrice();
            this.status = room.getStatus();
            this.roomImagePath = "/images/" + room.getRoomImage()   ;
            this.timeSlots = room.getTimeSlots().stream()
                    .map(timeSlot -> new TimeSlotResponse.DetailDTO(timeSlot))
                    .toList();
        }
    }
}
