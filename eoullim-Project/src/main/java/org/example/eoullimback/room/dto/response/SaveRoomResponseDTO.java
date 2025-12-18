package org.example.eoullimback.room.dto.response;

import org.example.eoullimback._common.enums.room.CapacityPolicy;
import org.example.eoullimback._common.enums.room.Category;
import org.example.eoullimback.file.RoomFile;
import org.example.eoullimback.room.Room;

import java.util.List;

public record SaveRoomResponseDTO(
        String name,
        String content,
        Category status,
        CapacityPolicy capacityPolicy,
        List<RoomFile> roomFile
) {
    public static SaveRoomResponseDTO from(Room room) {
        return new SaveRoomResponseDTO(
                room.getName(),
                room.getContent(),
                room.getCategory(),
                room.getCapacityPolicy(),
                room.getRoomFile()
        );
    }
}
