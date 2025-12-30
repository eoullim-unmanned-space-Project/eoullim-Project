package org.example.eoullimback.place;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.room.RoomRequest;
import org.example.eoullimback.room.RoomResponse;
import org.example.eoullimback.room.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final RoomService roomService;

    // 생성
    @GetMapping("/place/create")
    public String createForm() {

        return "place/create";
//        place-create
//        room-create
    }

    @PostMapping("/place/create")
    public String createProc(@Valid PlaceRequest.CreateDTO request) {
        Place place = placeService.placeCreate(request);

        return "redirect:/";
    }

    // 전체 조회
    @GetMapping("/")
    public String ListForm(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);
        PageResponse.PageDTO<Place, PlaceResponse.ListDTO> placePage = placeService.placeList(pageIndex, size, keyword);
        model.addAttribute("placePage", placePage);
        model.addAttribute("placeKeyword", keyword != null ? keyword : "");

        return "/map/map";
    }

    // 수정
    @GetMapping("/place/{placeId}/update")
    public String UpdateForm(Model model,
                             @PathVariable Long placeId
    ) {

        Place place = placeService.placeUpdateForm(placeId);
        model.addAttribute("place", place);

        return "place/update";
    }

    @PostMapping("/place/{placeId}/update")
    public String UpdateProc(@PathVariable Long placeId,
                             PlaceRequest.UpdateDTO request
    ) {
        Place place = placeService.placeUpdate(placeId, request);

        return "redirect:/place/" + placeId + "/update";
    }

    // 삭제
    @PostMapping("/place/{placeId}/delete")
    public String deleteProc(@PathVariable Long placeId) {

        placeService.placeDelete(placeId);

        return "redirect:/";
    }
}
