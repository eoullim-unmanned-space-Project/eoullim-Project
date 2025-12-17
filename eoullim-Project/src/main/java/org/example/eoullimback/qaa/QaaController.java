package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QaaController {

    private final QaaService qaaService;

    // Q&A 작성 화면 요청
    @GetMapping("/qaas")
    public String createQaa(HttpSession session) {
        session.getAttribute("sessionUser");
        return "qaas/create-form";
    }

    // Q&A 작성 요청 기능
    @PostMapping("/qaas")
    public String createQaaProc(HttpSession session, QaaRequest.SaveDto saveDto) {
//        User sessionUser = (User)session.getAttribute("sessionUser");
//        qaaService.createQaa(saveDto, sessionUser);
        return "redirect:/qaas/list";
    }

    // Q&A 목록 화면 요청
    @GetMapping("/qaas/list")
    public String listQaa(Model model
    ) {
//        List<QaaResponse.QaaListDto> qaaList = qaaService.qaaList();
//        model.addAttribute("qaaList", qaaList);
        return "qaas/list";
    }

//    // Q&A 수정 화면 요청
//    @GetMapping("/qaas/{id}")
//    public String updateQaa() {
//
//        return "qaas/update-form";
//    }
//
//    // Q&A 수정 요청 기능
//    @PutMapping("/qaas/{qaaId}")
//    public String updateQaaProc() {
//
//        return "redirect:/qaas/list";
//    }
//
//    // Q&A 삭제 요청 기능
//    @DeleteMapping("/qaas/{id}")
//    public String deleteQaa() {
//
//        return "qaas/list";
//    }
//
//    // Q&A 상세 보기 화면 요청
//    @GetMapping("/qaas/{id}")
//    public String detailQaa() {
//
//        return "qaas/detail";
//    }

}
