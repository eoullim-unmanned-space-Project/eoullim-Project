package org.example.eoullimback.admin.place;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceResponse;
import org.example.eoullimback.place.PlaceService;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomResponse;
import org.example.eoullimback.room.RoomService;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminPlaceController {

    private final PlaceService placeService;
    private final RoomService roomService;

    @GetMapping("/admin/place")
    @PreAuthorize("hasRole('ADMIN')")
    public String place(Model model,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(required = false) String placeKeyword,
                        @RequestParam(required = false) String roomKeyword,
                        @RequestParam(required = false) Category category,
                        @RequestParam(required = false) RoomStatus status
    ) {
        User user = userDetails.getUser();

        int pageIndex = Math.max(0, page - 1);
        PageResponse.PageDTO<Place, PlaceResponse.DetailDTO> placePage = placeService.placeAdminList(pageIndex, size, placeKeyword, category);
        model.addAttribute("placePage", placePage);
        PageResponse.PageDTO<Room, RoomResponse.AdminDetailDTO> roomList = roomService.roomAdminList(pageIndex, size, roomKeyword, status);

        model.addAttribute("placeKeyword", placeKeyword != null ? placeKeyword : "");
        model.addAttribute("roomKeyword", roomKeyword != null ? roomKeyword : "");
        model.addAttribute("roomList", roomList);
        model.addAttribute("category", category);

        return "/admin/place";
    }
}
