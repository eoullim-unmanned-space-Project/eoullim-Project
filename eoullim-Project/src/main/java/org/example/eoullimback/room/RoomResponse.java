package org.example.eoullimback.room;

import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.room_image.RoomImage;
import org.example.eoullimback.room_image.RoomImageResponse;

import java.util.List;

public class RoomResponse {

    @Data
    public static class ListDTO {
        Long roomId;
        String name;
        String content;
        RoomStatus status;
        String roomImage;

        public ListDTO(Room room, RoomImage images) {
            this.roomId = room.getId();
            this.name = room.getName();
            this.content = room.getContent();
            this.status = room.getStatus();
            this.roomImage =  (images != null ) ? images.getStoredName() : null;
        }
    }

    @Data
    public static class DetailDTO {
        String name;
        String content;
        int defaultPrice;
        RoomStatus status;
        List<RoomImageResponse.DetailDTO> roomImage;

        public DetailDTO(Room room, List<RoomImageResponse.DetailDTO> imageDTOs) {
            this.name = room.getName();
            this.content = room.getContent();
            this.defaultPrice = room.getDefaultPrice();
            this.status = room.getStatus();
            this.roomImage = imageDTOs;
        }
    }
}
