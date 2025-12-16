package org.example.eoullimback.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RoomController {

    // 생성
    @GetMapping("/place/{placeId}/room/save")
    public String saveForm(@PathVariable Long placeId) {

        return "room/save";
    }

    @PostMapping("/place/{placeId}/room/save")
    public String saveProc(@PathVariable Long placeId) {

        return "redirect:/place/detail";
    }

    // 목록 조회
    @GetMapping("/place/{placeId}/room")
    public String ListForm(@PathVariable Long placeId) {

        return "place/detail";
    }

    // 단건 조회
    @GetMapping("/room/{roomId}")
    public String DetailForm(@PathVariable Long roomId) {

        return "room/detail";
    }

    // 수정
    @GetMapping("/room/{roomId}/update")
    public String updateForm(@PathVariable Long roomId) {

        return "room/update";
    }

    @PostMapping("/room/{roomId}/update")
    public String updateProc(@PathVariable Long roomId) {

        return "redirect:/room/" + roomId;
    }

    // 삭제
    @PostMapping("/room/{roomId}/delete")
    public String deleteProc(@PathVariable Long roomId) {

        return "redirect:/place/detail";
    }
}
