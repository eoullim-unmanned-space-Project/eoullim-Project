package org.example.eoullimback.room;

import jakarta.validation.Valid;

import java.io.IOException;

public interface RoomService {
    Room createRoom(Long placeId, RoomRequest.@Valid CreateDTO createDTO) throws IOException ;
    void deleteRoom(Long roomId) throws IOException;
    Room updateRoom(Long roomId, RoomRequest.@Valid UpdateDTO updateDTO) throws IOException;
}
