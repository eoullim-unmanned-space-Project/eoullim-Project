package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QaaController {

    private final QaaService qaaService;

    // Q&A 화면 요청
    @GetMapping("/qaas/list")
    public String qaaList(Model model) {
//        List<QaaResponse.QaaListDto> qaaList = qaaService.qaaLists();
//        model.addAttribute("qaaList", qaaList);
        return "qaa/list";
    }
}
