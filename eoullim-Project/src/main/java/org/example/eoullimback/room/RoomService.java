package org.example.eoullimback.room;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    Room createRoom(Long placeId, RoomRequest.@Valid CreateDTO createDTO) throws IOException;
    List<RoomResponse.ListDTO> roomList(Long placeId);
    RoomResponse.DetailDTO DetailRoom(Long roomId);
    Room updateRoom(Long roomId, RoomRequest.@Valid UpdateDTO updateDTO) throws IOException;
    void deleteRoom(Long roomId) throws IOException;
}
