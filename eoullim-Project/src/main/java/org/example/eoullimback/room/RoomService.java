package org.example.eoullimback.room;

import java.util.List;

public interface RoomService {
    List<RoomResponse.ListDTO> roomList(Long placeId);
}
