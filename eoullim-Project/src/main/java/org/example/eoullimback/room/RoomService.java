package org.example.eoullimback.room;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

public interface RoomService {

    Room createRoom(Long placeId, RoomRequest.CreateDTO createDTO);
    List<RoomResponse.ListDTO> roomList(Long placeId);
    RoomResponse.DetailDTO DetailRoom(Long roomId);
    Room updateRoom(Long roomId, RoomRequest.UpdateDTO updateDTO);
    void deleteRoom(Long roomId);
}
