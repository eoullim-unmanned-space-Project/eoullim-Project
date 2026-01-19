package org.example.eoullimback.qna;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback.comment.CommentResponse;
import org.example.eoullimback.comment.CommentService;
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
public class QnaController {

    private final QnaService qnaService;
    private final CommentService commentService;
    private final QnaRepository qnaRepository;

    // Q&A 전체 목록 화면 요청
    // http://localhost:8080/qnas
    @GetMapping("/qnas")
    public String listQna(Model model,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);

        PageResponse.PageDTO<Qna, QnaResponse.ListDTO> qnaPage = qnaService.qnaListFindAll(pageIndex, size, keyword);

        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("keyword", keyword != null ? keyword: "");
        return "qna/list";
    }

    // Q&A 상세 보기 화면 요청
    // http://localhost:8080/qnas/{id}
    @GetMapping("/qnas/{id}")
    public String detailQnaForm(@PathVariable Long id,
                                Model model,
                                HttpSession session) {

        qnaService.increaseViewCount(id);

        QnaResponse.DetailDTO qna = qnaService.qnaDetailResponse(id);

        User sessionUser = (User) session.getAttribute("sessionUser");
        Long sessionUserId = sessionUser != null ? sessionUser.getId() : null;

        boolean isOwner = sessionUser != null
                && qna.getUserId() != null
                && qna.getUserId().equals(sessionUserId);

        Long editingCommentId = (Long) session.getAttribute("commentId");

        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(id, sessionUserId, editingCommentId);

        model.addAttribute("qna", qna);
        model.addAttribute("commentList", commentList);
        model.addAttribute("isOwner", isOwner);

        return "qna/detail";
    }

    // 마이프로필 Q&A 작성
    @PostMapping("/my/qna")
    public String createMyQna(HttpSession session,
                              @Valid QnaRequest.CreateDTO request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.createQna(request, sessionUser);
        return "redirect:/my/qna";
    }

    // 마이프로필 qna 리스트 화면
    @GetMapping("/my/qna")
    public String myQnaPage(HttpSession session, Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        int pageIndex = Math.max(0, page - 1);

        QnaResponse.ListPageDTO qnaPage =
                qnaService.myQnaList(sessionUser.getId(), pageIndex, size);

        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("user", sessionUser);

        return "user/qna";
    }

    // 마이프로필 qna 세부사항
    @GetMapping("/my/qna/{id}")
    public String myQnaDetail(@PathVariable Long id,
                              HttpSession session,
                              Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.increaseViewCount(id);
        QnaResponse.DetailDTO qna = qnaService.qnaDetailResponse(id);

        if (!qna.getUserId().equals(sessionUser.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        Long editingCommentId = (Long) session.getAttribute("commentId");
        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(id, sessionUser.getId(), editingCommentId);

        model.addAttribute("qna", qna);
        model.addAttribute("commentList", commentList);
        model.addAttribute("user", sessionUser);

        return "user/qna-detail";
    }

    // 마이프로필 qna 수정 요청 폼
    @GetMapping("/my/qna/{id}/edit")
    public String editForm(@PathVariable Long id,
                           HttpSession session,
                           Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        QnaResponse.UpdateFormDTO qna = qnaService.findUpdateForm(id, sessionUser.getId());
        model.addAttribute("qna", qna);
        model.addAttribute("user", sessionUser);
        return "qna/update-form";
    }

    // Q&A 메인 화면 수정 요청 기능
    // http://localhost:8080/qnas/{id}/update
    @PostMapping("/qnas/{id}/update")
    public String updateQna(@PathVariable Long id,
                            @Valid QnaRequest.UpdateDTO updateRequest,
                            HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);
        qnaService.update(id, updateRequest, sessionUser);
        return "redirect:/my/qna/" + id;
    }

    // 마이프로필 qna 삭제
    @PostMapping("/my/qna/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.delete(id, sessionUser);
        return "redirect:/my/qna";
    }
}
