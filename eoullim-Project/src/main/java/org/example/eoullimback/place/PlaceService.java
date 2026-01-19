package org.example.eoullimback.place;

import jakarta.servlet.http.HttpSession;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.room.RoomRequest;
import org.example.eoullimback.user_auth.user.User;

import java.util.List;

public interface PlaceService {
    // 사용자용
    PageResponse.PageDTO<Place, PlaceResponse.ListDTO> placeList(int pageIndex, int size, String keyword);
    List<PlaceResponse.ListDTO> newPlace();

    // 관리자용
    Place placeCreate(PlaceRequest.CreateDTO request);
    PageResponse.PageDTO<Place, PlaceResponse.DetailDTO> placeAdminList(int pageIndex, int size, String keyword);
    Place placeUpdateForm(Long placeId);
    Place placeUpdate(Long placeId, PlaceRequest.UpdateDTO request);
    void placeDelete(Long placeId);
}
