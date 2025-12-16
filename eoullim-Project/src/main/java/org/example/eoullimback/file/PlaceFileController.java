package org.example.eoullimback.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PlaceFileController {

//    // 업로드
//    @GetMapping("/place/save/file")
//    public String saveFileForm() {
//
//        return "place/save";
//    }
//
//    @PostMapping("/place/save/file")
//    private String saveFileProc() {
//
//        return "redirect:/place/save";
//    }
//
//    // 파일 목록 조회
//    @GetMapping("/place/{placeId}")
//    public String listFilesForm(@PathVariable Long placeId) {
//
//        return "place/detail";
//    }
//
//    // 파일 단건 조회
//    @GetMapping("/place/list")
//    public String FirstFileForm() {
//
//        return "place/detail";
//    }
//
//    // 수정
//    @GetMapping("/place/{placeId}/update/file")
//    public String updateFileForm(@PathVariable Long placeId) {
//        return "place/update";
//    }
//
//    @PostMapping("/place/{placeId}/update/file")
//    public String updateFileProc(@PathVariable Long placeId, @PathVariable Long fileId) {
//
//        return "redirect:place/" + placeId;
//    }
//
//    // 삭제
//    @PostMapping("/place/{placeId}/delete/{fileId}")
//    public String deleteFileProc(@PathVariable Long placeId, @PathVariable Long fileId) {
//
//        return "redirect:/place/" + placeId;
//    }
}
