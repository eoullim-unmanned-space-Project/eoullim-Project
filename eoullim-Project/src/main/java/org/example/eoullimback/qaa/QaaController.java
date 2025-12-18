package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageDTO;
import org.example.eoullimback.qaa.dto.request.QaaSaveRequest;
import org.example.eoullimback.qaa.dto.request.QaaUpdateRequest;
import org.example.eoullimback.qaa.dto.response.QaaListResponse;
import org.example.eoullimback.qaa.dto.response.QaaUpdateFormResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class QaaController {

    private final QaaService qaaService;

    // Q&A 작성 화면 요청
    // http://localhost:8080/qaas/new
    @GetMapping("/qaas/new")
    public String createQaaForm(HttpSession session) {
        session.getAttribute("sessionUser");
        return "qaas/create-form";
    }

    // Q&A 작성 요청 기능
    // http://localhost:8080/qaas
    @PostMapping("/qaas")
    public String createQaa(
            HttpSession session,
            @Valid QaaSaveRequest request
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qaaService.save(request, sessionUser);

        return "redirect:/qaas";
    }

    // Q&A 목록 화면 요청
    // http://localhost:8080/qaas
    @GetMapping("/qaas")
    public String listQaa(Model model,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);
        PageDTO<QaaListResponse> qaaPage = qaaService.qaaListFindAll(pageIndex, size, keyword);

        model.addAttribute("qaaPage", qaaPage);
        model.addAttribute("keyword", keyword != null ? keyword: "");

        return "qaas/list";
    }

    // Q&A 상세 보기 화면 요청
    // http://localhost:8080/qaas/{id}
    @GetMapping("/qaas/{id}")
    public String detailQaaForm(@PathVariable Long id, Model model) {
        model.addAttribute("qaa", qaaService.findById(id));
        return "qaas/detail";
    }

    // Q&A 수정 화면 요청
    // http://localhost:8080/qaas/{id}/edit
    @PostMapping("/qaas/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("qaa", qaaService.findUpdateForm(id));
        return "qaas/update-form";
    }

    // Q&A 수정 요청 기능
    // http://localhost:8080/qaas/{id}
    @PostMapping("/qaas/{id}")
    public String updateQaa(
            @PathVariable Long id,
            QaaUpdateRequest updateRequest,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        QaaUpdateFormResponse qaa = qaaService.update(id, updateRequest, sessionUser);
        return "redirect:/qaas/{id}";
    }

    // Q&A 삭제 요청 기능
    // http://localhost:8080/qaas/{id}/delete
    @PostMapping("/qaas/{id}/delete")
    public String deleteQaa(
            @PathVariable Long id,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        qaaService.delete(id, sessionUser);
        return "redirect:/qaas";
    }
}
