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
import org.example.eoullimback.user_auth.user.User;
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
public class AdminQnaController {

    private final QnaService qnaService;
    private final CommentService commentService;
    private final QnaRepository qnaRepository;

    // admin qna 세부사항
    @GetMapping("/admin/qnas")
    public String adminQnaList(HttpSession session,
                               Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String keyword
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        int pageIndex = Math.max(0, page - 1);

        PageResponse.PageDTO<Qna, QnaResponse.ListDTO> qnaPage = qnaService.adminQnaListFindAll(sessionUser.getId(), pageIndex, size, keyword);

        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("keyword", keyword != null ? keyword: "");
        return "admin/qna/qna";
    }

    // 관리자 qna 상세보기
    @GetMapping("/admin/qnas/{qnaId}")
    public String adminQnaDetail(HttpSession session,
                                 Model model,
                                 @PathVariable("qnaId") Long qnaId) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.increaseViewCount(qnaId);

        QnaResponse.DetailDTO qna = qnaService.qnaDetailResponse(qnaId);

        Long sessionUserId = sessionUser.getId();
        Long editingAdminId = (Long) session.getAttribute("adminId");

        List<CommentResponse.ListDTO> commentList =
                commentService.listComment(qnaId, sessionUserId, editingAdminId);

        model.addAttribute("qna", qna);
        model.addAttribute("commentList", commentList);

        return "admin/qna/qna-detail";
    }

    // 관리자 qna 삭제
    @PostMapping("/admin/qnas/{qnaId}/delete")
    public String adminDeleteQna(@PathVariable("qnaId") Long qnaId,
                                 HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        qnaService.deleteAsAdmin(qnaId, sessionUser);

        return "redirect:/admin/qnas";
    }

    // 관리자 대시보드 문의 개수
    @GetMapping("/api/admin/qnas/count")
    @ResponseBody
    public Map<String, Object> todayQnaCount(HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long count = qnaRepository.countToday(start, end);

        return Map.of("todayCount", count);
    }

    @PostMapping("/admin/qnas/{qnaId}/comments/new")
    public String createAdminComment(@PathVariable Long qnaId,
                                     CommentRequest.createDTO saveRequest,
                                     HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        saveRequest.setQnaId(qnaId);
        commentService.createCommentAsAdmin(saveRequest, sessionUser.getId());

        return "redirect:/admin/qnas/" + qnaId;
    }

    @PostMapping("/admin/comments/{id}/delete")
    public String adminDeleteComment(@PathVariable Long id, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        Long qnaId = commentService.deleteCommentAsAdmin(id, sessionUser.getId());

        return "redirect:/admin/qnas/" + qnaId;
    }
}
