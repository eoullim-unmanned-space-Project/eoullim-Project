package org.example.eoullimback.room.dto.response;

import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.file.RoomFile;
import org.example.eoullimback.room.Room;

import java.util.List;

public record SaveRoomResponseDTO(
        String name,
        String content,
        RoomStatus status,
        List<RoomFile> roomFile
) {
    public static SaveRoomResponseDTO from(Room room) {
        return new SaveRoomResponseDTO(
                room.getName(),
                room.getContent(),
                room.getStatus(),
                room.getRoomFile()
        );
    }
}
