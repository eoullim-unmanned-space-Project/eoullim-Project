package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageDTO;
import org.example.eoullimback.comment.CommentServiceImpl;
import org.example.eoullimback.comment.dto.response.CommentListResponse;
import org.example.eoullimback.qaa.dto.request.QaaSaveRequest;
import org.example.eoullimback.qaa.dto.request.QaaUpdateRequest;
import org.example.eoullimback.qaa.dto.response.QaaDetailResponse;
import org.example.eoullimback.qaa.dto.response.QaaListResponse;
import org.example.eoullimback.qaa.dto.response.QaaUpdateFormResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QaaController {

    private final QaaServiceImpl qaaService;
    private final CommentServiceImpl commentService;

    // Q&A 작성 화면 요청
    // http://localhost:8080/qaas/new
    @GetMapping("/qaas/new")
    public String createQaaForm(HttpSession session) {
        session.getAttribute("sessionUser");
        return "qaas/create-form";
    }

    // Q&A 작성 요청 기능
    // http://localhost:8080/qaas/new
    @PostMapping("/qaas/new")
    public String createQaa(
            HttpSession session,
            @Valid QaaSaveRequest request
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qaaService.createQaa(request, sessionUser);
        return "redirect:/";
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
    public String detailQaaForm(@PathVariable(name = "id") Long qaaId,
                                Model model,
                                HttpSession session
    ) {
        qaaService.increaseViewCount(qaaId);
        QaaDetailResponse qaa = qaaService.qaaDetailResponse(qaaId);

        User sessionUser = (User) session.getAttribute("sessionUser");
        boolean isOwner = false;
        if(sessionUser != null && qaa.userId() != null) {
            isOwner = qaa.userId().equals(sessionUser.getId());
        }

        Long sessionUserId = sessionUser != null ? sessionUser.getId() : null;
        List<CommentListResponse> commentList = commentService.listComment(qaaId, sessionUserId);

        model.addAttribute("isOwner", isOwner);
        model.addAttribute("qaa", qaa);
        model.addAttribute("commentList", commentList);

        return "qaa/detail";
    }

    // Q&A 수정 화면 요청
    // http://localhost:8080/qaas/{id}/update
    @GetMapping("/qaas/{id}/update")
    public String editForm(@PathVariable Long id,
                           Model model,
                           HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        QaaUpdateFormResponse qaa = qaaService.findUpdateForm(id, sessionUser.getId());
        model.addAttribute("qaa", qaa);
        return "qaas/update-form";
    }

    // Q&A 수정 요청 기능
    // http://localhost:8080/qaas/{id}/update
    @PostMapping("/qaas/{id}/update")
    public String updateQaa(@PathVariable Long id,
                            QaaUpdateRequest updateRequest,
                            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qaaService.update(id, updateRequest, sessionUser);
        return "redirect:/qaas/{id}";
    }

    // Q&A 삭제 요청 기능
    // http://localhost:8080/qaas/{id}/delete
    @PostMapping("/qaas/{id}/delete")
    public String deleteQaa(@PathVariable Long id,
                            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qaaService.delete(id, sessionUser);
        return "redirect:/";
    }
}
