package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.comment.CommentResponse;
import org.example.eoullimback.comment.CommentServiceImpl;
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
        return "qaa/create-form";
    }

    // Q&A 작성 요청 기능
    // http://localhost:8080/qaas/new
    @PostMapping("/qaas/new")
    public String createQaa(
            HttpSession session,
            @Valid QaaRequest.CreateDTO request
    ) {

//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);
        qaaService.createQaa(request, sessionUser);
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

        PageResponse.PageDTO<Qaa, QaaResponse.ListDTO> qaaPage = qaaService.qaaListFindAll(pageIndex, size, keyword);

        model.addAttribute("qaaPage", qaaPage);
        model.addAttribute("keyword", keyword != null ? keyword: "");
        return "qaa/list";
    }

    // Q&A 상세 보기 화면 요청
    // http://localhost:8080/qaas/{id}
    @GetMapping("/qaas/{id}")
    public String detailQaaForm(@PathVariable(name = "id") Long qaaId,
                                Model model,
                                HttpSession session
    ) {
        qaaService.increaseViewCount(qaaId);

        QaaResponse.DetailDTO qaa = qaaService.qaaDetailResponse(qaaId);

//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);

        boolean isOwner = sessionUser != null
                && qaa.getUserId() != null
                && qaa.getUserId().equals(sessionUser.getId());

        Long sessionUserId = sessionUser != null ? sessionUser.getId() : null;

        Long commentId = (Long) session.getAttribute("commentId");

        List<CommentResponse.ListDTO> commentList = commentService.listComment(qaaId, sessionUserId, commentId);

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
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);

        QaaResponse.UpdateFormDTO qaa = qaaService.findUpdateForm(id, sessionUser.getId());
        model.addAttribute("qaa", qaa);
        return "qaa/update-form";
    }

    // Q&A 수정 요청 기능
    // http://localhost:8080/qaas/{id}/update
    @PostMapping("/qaas/{id}/update")
    public String updateQaa(@PathVariable Long id,
                            @Valid QaaRequest.UpdateDTO updateRequest,
                            HttpSession session
    ) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);
        qaaService.update(id, updateRequest, sessionUser);
        return "redirect:/qaas";
    }

    // Q&A 삭제 요청 기능
    // http://localhost:8080/qaas/{id}/delete
    @PostMapping("/qaas/{id}/delete")
    public String deleteQaa(@PathVariable Long id,
                            HttpSession session
    ) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);
        qaaService.delete(id, sessionUser);
        return "redirect:/qaas";
    }

    // TODO : 유저 더미 (삭제 예정)
    private User getLoginUserId(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            User dummyUser = new User();
            dummyUser.setId(1L);
            session.setAttribute("sessionUser", dummyUser);
            return dummyUser;
        }
        return sessionUser;
    }
}
