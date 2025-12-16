package org.example.eoullimback.place;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    // 생성
    @GetMapping("/place/save")
    public String saveForm() {
        return "place/save";
    }

    @PostMapping("/place/save")
    public String saveProc() {

        return "redirect:place/list";
    }

    // 전체 조회
    @GetMapping("/place/list")
    public String ListForm() {
        return "place/list";
    }


    // 단건 조회
    @GetMapping("/place/{placeId}")
    public String DetailForm(@PathVariable Long placeId) {

        return "place/detail";
    }

    // 수정
    @GetMapping("/place/{placeId}/update")
    public String UpdateForm(@PathVariable Long placeId) {
        return "place/update";
    }

    @PostMapping("/place/{placeId}/update")
    public String UpdateProc(@PathVariable Long placeId) {

        return "redirect:place/" + placeId;
    }

    // 삭제
    @PostMapping("/place/{placeId}/delete")
    public String deleteProc(@PathVariable Long placeId) {

        return "redirect:/place/list";
    }
}
