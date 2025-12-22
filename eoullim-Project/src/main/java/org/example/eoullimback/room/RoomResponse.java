package org.example.eoullimback.room;

import lombok.Data;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.file.RoomFile;

import java.util.List;

public class RoomResponse {

    @Data
    public static class ListDTO {
        String name;
        String content;
        RoomStatus status;
        List<RoomFile> roomFile;

        public ListDTO(Room room) {
            this.name = room.getName();
            this.content = room.getContent();
            this.status = room.getStatus();
            this.roomFile = room.getRoomFile();
        }
    }
}
