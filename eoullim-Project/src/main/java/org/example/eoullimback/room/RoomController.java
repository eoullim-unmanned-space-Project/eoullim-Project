package org.example.eoullimback.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    
    /**
     * 관리자가 생성하는 화면
     */
    @GetMapping("/place/{placeId}/room/create")
    public String createView(@PathVariable Long placeId) {

        return "room/create";
    }

    /**
     * 기능: 관리자가 생성하는 룸 생성 - 완료
     */
    @PostMapping("/place/{placeId}/room/create")
    public String createRoom(@PathVariable Long placeId, @ModelAttribute @Valid RoomRequest.CreateDTO createDTO) throws IOException {

        Room room = roomService.createRoom(placeId, createDTO);

        return "redirect:/place/detail" + room.getId();
    }

    /**
     * 화면: 수정
     */
    @GetMapping("/room/{roomId}/update")
    public String updateView(@PathVariable Long roomId) {


        return "room/update";
    }

    /**
     * 기능: 수정 - 완료
     * - 룸의 이름, 내용, 이미지만 변경
     * - 타임슬롯 변경은 x 안됨
     */
    @PostMapping("/room/{roomId}/update")
    public String updateRoom(@PathVariable Long roomId, @ModelAttribute @Valid RoomRequest.UpdateDTO updateDTO) throws IOException {

        Room room = roomService.updateRoom(roomId, updateDTO);

        return "redirect:/room/" + room.getId();
    }

    /**
     * 기능: 삭제 - 완료
     */
    @PostMapping("/room/{roomId}/delete")
    public String deleteRoom(@PathVariable Long roomId) throws IOException {

        roomService.deleteRoom(roomId);

        return "redirect:/place/";
    }


    /**
     * 단건 조회 - VIEW
     * 1. 룸 정보
     * 2. 룸 파일 이미지
     * 3. 타임슬롯 + 아이템
     */
    @GetMapping("/room/{roomId}/detail")
    public String DetailRoom(@PathVariable Long roomId, Model model) {

        RoomResponse.DetailDTO room = roomService.DetailRoom(roomId);

        model.addAttribute("room", room);

        return "room/detail";
    }
}
