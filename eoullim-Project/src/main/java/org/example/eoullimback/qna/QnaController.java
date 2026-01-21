package org.example.eoullimback.qna;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback.comment.CommentResponse;
import org.example.eoullimback.comment.CommentService;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // Q&A 전체 목록 화면 요청
    // http://localhost:8080/qnas
    @GetMapping("/public/qnas")
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
    @GetMapping("/public/qnas/{id}")
    public String detailQnaForm(@PathVariable Long id,
                                Model model,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                HttpSession session) {

        qnaService.increaseViewCount(id);

        QnaResponse.DetailDTO qna = qnaService.qnaDetailResponse(id);

        User user = (userDetails != null) ? userDetails.getUser() : null;
        Long sessionUserId = (user != null) ? user.getId() : null;

        boolean isOwner = user != null
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
    @PostMapping("/user/qna")
    @PreAuthorize("hasRole('USER')")
    public String createMyQna(@Valid QnaRequest.CreateDTO request,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.createQna(request, user);
        return "redirect:/user/qna";
    }

    // 마이프로필 qna 리스트 화면
    @GetMapping("/user/qna")
    @PreAuthorize("hasRole('USER')")
    public String myQnaPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                            Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        int pageIndex = Math.max(0, page - 1);

        QnaResponse.ListPageDTO qnaPage =
                qnaService.myQnaList(user.getId(), pageIndex, size);

        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("user", user);

        return "user/qna";
    }

    // 마이프로필 qna 세부사항
    @GetMapping("/user/qna/{id}")
    @PreAuthorize("hasRole('USER')")
    public String myQnaDetail(@PathVariable Long id,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              HttpSession session,
                              Model model) {
        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.increaseViewCount(id);
        QnaResponse.DetailDTO qna = qnaService.qnaDetailResponse(id);

        if (!qna.getUserId().equals(user.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        Long editingCommentId = (Long) session.getAttribute("commentId");
        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(id, user.getId(), editingCommentId);

        model.addAttribute("qna", qna);
        model.addAttribute("commentList", commentList);
        model.addAttribute("user", user);

        return "user/qna-detail";
    }

    // 마이프로필 qna 수정 요청 폼
    @GetMapping("/user/qna/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {
        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        QnaResponse.UpdateFormDTO qna = qnaService.findUpdateForm(id, user.getId());
        model.addAttribute("qna", qna);
        model.addAttribute("user", user);
        return "qna/update-form";
    }

    // Q&A 메인 화면 수정 요청 기능
    // http://localhost:8080/qnas/{id}/update
    @PostMapping("/qnas/{id}/update")
    @PreAuthorize("hasRole('USER')")
    public String updateQna(@PathVariable Long id,
                            @Valid QnaRequest.UpdateDTO updateRequest,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);
        qnaService.update(id, updateRequest, user);
        return "redirect:/user/qna/" + id;
    }

    // 마이프로필 qna 삭제
    @PostMapping("/user/qna/{id}/delete")
    @PreAuthorize("hasRole('USER')")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.delete(id, user);
        return "redirect:/user/qna";
    }
}
