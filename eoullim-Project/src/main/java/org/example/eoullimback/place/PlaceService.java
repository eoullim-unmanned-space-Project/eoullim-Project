package org.example.eoullimback.place;

import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.room.dto.request.SaveRoomRequestDTO;

public interface PlaceService {
    Place placeCreate(PlaceRequest.CreateDTO request);
    PageResponse.PageDTO<Place, PlaceResponse.ListDTO> placeList(int pageIndex, int size, String keyword);
    PlaceResponse.DetailDTO placeDetail(Long placeId);
    PlaceResponse placeUpdateView(Long placeId);
    PlaceResponse placeUpdate(Long placeId, SaveRoomRequestDTO request);
    void placeDelete(Long placeId);
}
