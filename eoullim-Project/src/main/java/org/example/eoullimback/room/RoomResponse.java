package org.example.eoullimback.room;

import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.room_image.RoomImage;
import java.util.List;

public class RoomResponse {

    @Data
    public static class ListDTO {
        Long roomId;
        String name;
        String content;
        RoomStatus status;
        List<String> roomImage;

        public ListDTO(Room room, List<RoomImage> images) {
            this.roomId = room.getId();
            this.name = room.getName();
            this.content = room.getContent();
            this.status = room.getStatus();
            this.roomImage = images.stream().map(RoomImage::getStoredName).toList();
        }
    }
}
