package org.example.eoullimback.place;

import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.room.RoomRequest;

import java.util.List;

public interface PlaceService {
    Place placeCreate(PlaceRequest.CreateDTO request);
    PageResponse.PageDTO<Place, PlaceResponse.ListDTO> placeList(int pageIndex, int size, String keyword);
    List<PlaceResponse.ListDTO> newPlace();
    Place placeUpdateForm(Long placeId);
    Place placeUpdate(Long placeId, PlaceRequest.UpdateDTO request);
    void placeDelete(Long placeId);
}
