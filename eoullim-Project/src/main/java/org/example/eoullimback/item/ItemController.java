package org.example.eoullimback.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    // 생성
    @GetMapping("/room/{roomId}/item/create")
    public String saveForm(@PathVariable Long roomId) {

        return "item/create";
    }

    @PostMapping("/room/{roomId}/item/create")
    public String saveProc(@PathVariable Long roomId) {

        return "redirect:/room/detail";
    }

    // 목록 조회
    @GetMapping("/room/{roomId}/item")
    public String ListForm(@PathVariable Long roomId) {

        return "room/detail";
    }

    // 단건 조회
    @GetMapping("/item/{itemId}")
    public String DetailForm(@PathVariable Long itemId) {

        return "room/detail";
    }

    // 수정
    @GetMapping("/item/{itemId}/update")
    public String updateForm(@PathVariable Long itemId) {

        return "item/update";
    }

    @PostMapping("/item/{itemId}/update")
    public String updateProc(@PathVariable Long itemId) {

        return "redirect:/item/" + itemId;
    }

    // 삭제
    @PostMapping("/item/{itemId}/delete")
    public String deleteProc(@PathVariable Long itemId) {

        return "redirect:/room/detail";
    }
}
