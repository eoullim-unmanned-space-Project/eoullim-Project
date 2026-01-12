package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
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

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);
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
    public String detailQaaForm(@PathVariable Long id,
                                Model model,
                                HttpSession session) {

        qaaService.increaseViewCount(id);

        QaaResponse.DetailDTO qaa = qaaService.qaaDetailResponse(id);

        User sessionUser = (User) session.getAttribute("sessionUser");
        Long sessionUserId = sessionUser != null ? sessionUser.getId() : null;

        boolean isOwner = sessionUser != null
                && qaa.getUserId() != null
                && qaa.getUserId().equals(sessionUserId);

        Long editingCommentId = (Long) session.getAttribute("commentId");

        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(id, sessionUserId, editingCommentId);

        model.addAttribute("qaa", qaa);
        model.addAttribute("commentList", commentList);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("sessionUser", sessionUser); // null 가능

        return "qaa/detail";
    }

    @GetMapping("/user/qna/{id}/edit")
    public String editForm(@PathVariable Long id,
                           HttpSession session,
                           Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        QaaResponse.UpdateFormDTO qaa = qaaService.findUpdateForm(id, sessionUser.getId());
        model.addAttribute("qaa", qaa);
        model.addAttribute("user", sessionUser);
        return "qaa/update-form";
    }

    @PostMapping("/user/qna/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid QaaRequest.UpdateDTO request,
                         HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qaaService.update(id, request, sessionUser);
        return "redirect:/user/qna/" + id;
    }

    // Q&A 수정 요청 기능
    // http://localhost:8080/qaas/{id}/update
    @PostMapping("/qaas/{id}/update")
    public String updateQaa(@PathVariable Long id,
                            @Valid QaaRequest.UpdateDTO updateRequest,
                            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);
        qaaService.update(id, updateRequest, sessionUser);
        return "redirect:/qaas";
    }

    // Q&A 삭제 요청 기능
    // http://localhost:8080/qaas/{id}/delete
    @PostMapping("/qaas/{id}/delete")
    public String deleteQaa(@PathVariable Long id,
                            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        qaaService.delete(id, sessionUser);
        return "redirect:/qaas";
    }

    @GetMapping("/user/qna")
    public String myQnaPage(HttpSession session, Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        int pageIndex = Math.max(0, page - 1);

        QaaResponse.ListPageDTO qaaPage =
                qaaService.myQaaList(sessionUser.getId(), pageIndex, size);

        model.addAttribute("qaaPage", qaaPage);
        model.addAttribute("user", sessionUser);       // 사이드바용
        model.addAttribute("sessionUser", sessionUser);

        return "user/qna";
    }

    // 내 Q&A 작성
    @PostMapping("/user/qna")
    public String createMyQna(HttpSession session,
                              @Valid QaaRequest.CreateDTO request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qaaService.createQaa(request, sessionUser);
        return "redirect:/user/qna";
    }

    @GetMapping("/user/qna/{id}")
    public String myQnaDetail(@PathVariable Long id,
                              HttpSession session,
                              Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qaaService.increaseViewCount(id);
        QaaResponse.DetailDTO qaa = qaaService.qaaDetailResponse(id);

        if (!qaa.getUserId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        Long editingCommentId = (Long) session.getAttribute("commentId");
        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(id, sessionUser.getId(), editingCommentId);

        model.addAttribute("qaa", qaa);
        model.addAttribute("commentList", commentList);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("user", sessionUser);

        return "user/qna-detail";
    }



    @PostMapping("/user/qna/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qaaService.delete(id, sessionUser);
        return "redirect:/user/qna";
    }
}
