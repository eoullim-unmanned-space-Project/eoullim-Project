package org.example.eoullimback.room;

import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.room.RoomStatus;

import java.util.List;

public interface RoomService {

    Room createRoom( RoomRequest.CreateDTO createDTO);
    List<RoomResponse.ListDTO> roomList(Long placeId);
    RoomResponse.DetailDTO detailRoom(Long roomId);
    Room updateRoom(Long roomId, RoomRequest.UpdateDTO updateDTO);
    void deleteRoom(Long roomId);

    PageResponse.PageDTO<Room, RoomResponse.AdminDetailDTO> roomAdminList(int pageIndex, int size, String keyword, RoomStatus status);
}
