package org.example.eoullimback.place;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.room.dto.request.SaveRoomRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    // 생성
    @GetMapping("/place/create")
    public String createForm() {

        return "place/create";
    }

    @PostMapping("/place/create")
    public String createProc(PlaceRequest.CreateDTO request) {
        Place place = placeService.placeCreate(request);

        return "redirect:/place/list";
    }

    // 전체 조회
    @GetMapping("/place/list")
    public String ListForm(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);
        PageResponse.PageDTO<Place, PlaceResponse.ListDTO> PlacePage = placeService.placeList(pageIndex, size, keyword);

        return "place/list";
    }


    // 단건 조회
    @GetMapping("/place/{placeId}")
    public String DetailForm(@PathVariable Long placeId) {
        PlaceResponse.DetailDTO place = placeService.placeDetail(placeId);

        return "place/detail";
    }

    // 수정
    @GetMapping("/place/{placeId}/update")
    public String UpdateForm(@PathVariable Long placeId
    ) {
        PlaceResponse place = placeService.placeUpdateView(placeId);

        return "place/update";
    }

    @PostMapping("/place/{placeId}/update")
    public String UpdateProc(@PathVariable Long placeId,
                             SaveRoomRequestDTO request
    ) {
        PlaceResponse place = placeService.placeUpdate(placeId, request);

        return "redirect:/place/" + placeId;
    }

    // 삭제
    @PostMapping("/place/{placeId}/delete")
    public String deleteProc(@PathVariable Long placeId) {

        placeService.placeDelete(placeId);

        return "redirect:/place/list";
    }
}
