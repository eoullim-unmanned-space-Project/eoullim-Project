package org.example.eoullimback.admin.qna;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback.comment.CommentRequest;
import org.example.eoullimback.comment.CommentResponse;
import org.example.eoullimback.comment.CommentService;
import org.example.eoullimback.qna.Qna;
import org.example.eoullimback.qna.QnaRepository;
import org.example.eoullimback.qna.QnaResponse;
import org.example.eoullimback.qna.QnaService;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminQnaCommentController {

    private final QnaService qnaService;
    private final CommentService commentService;
    private final QnaRepository qnaRepository;

    // admin qna 리스트
    @GetMapping("/admin/qnas")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminQnaList(@AuthenticationPrincipal CustomUserDetails userDetails,
                               Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String keyword) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        int pageIndex = Math.max(0, page - 1);

        PageResponse.PageDTO<Qna, QnaResponse.ListDTO> qnaPage = qnaService.adminQnaListFindAll(user.getId(), pageIndex, size, keyword);

        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("keyword", keyword != null ? keyword: "");
        return "admin/qnacomment/qna";
    }

    // 관리자 qna 상세보기
    @GetMapping("/admin/qnas/{qnaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminQnaDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 HttpSession session,
                                 Model model,
                                 @PathVariable("qnaId") Long qnaId) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.increaseViewCount(qnaId);

        QnaResponse.DetailDTO qna = qnaService.qnaDetailResponse(qnaId);

        Long sessionUserId = user.getId();
        Long editingAdminId = (Long) session.getAttribute("adminId");

        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(qnaId, sessionUserId, editingAdminId);

        model.addAttribute("qna", qna);
        model.addAttribute("commentList", commentList);

        return "admin/qnacomment/qna-detail";
    }

    // 관리자 qna 삭제
    @PostMapping("/admin/qnas/{qnaId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDeleteQna(@PathVariable("qnaId") Long qnaId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.deleteAsAdmin(qnaId, user);

        return "redirect:/admin/qnas";
    }

    // 관리자 대시보드 문의 개수
    @GetMapping("/api/admin/qnas/count")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, Object> todayQnaCount(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long count = qnaRepository.countToday(start, end);

        return Map.of("todayCount", count);
    }

    // Qna 댓글 생성 (관리자)
    @PostMapping("/admin/qnas/{qnaId}/comments/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAdminComment(@PathVariable Long qnaId,
                                     CommentRequest.createDTO saveRequest,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        saveRequest.setQnaId(qnaId);
        commentService.createCommentAsAdmin(saveRequest, user.getId());

        return "redirect:/admin/qnas/" + qnaId;
    }

    // Qna 댓글 삭제 (관리자)
    @PostMapping("/admin/comments/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDeleteComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        Long qnaId = commentService.deleteCommentAsAdmin(id, user.getId());

        return "redirect:/admin/qnas/" + qnaId;
    }
}
