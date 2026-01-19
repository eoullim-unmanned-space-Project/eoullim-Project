package org.example.eoullimback.admin.notice;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.notice.Notice;
import org.example.eoullimback.notice.NoticeRequest;
import org.example.eoullimback.notice.NoticeResponse;
import org.example.eoullimback.notice.NoticeService;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    //  관리자 공지 목록(관리자만)
    @GetMapping("/admin/notices")
    public String adminListNotice(HttpSession session,
                                  Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(required = false) String keyword) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.assertAdmin(sessionUser);

        int pageIndex = Math.max(0, page - 1);

        PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticePage =
                noticeService.adminNoticeListFindAll(sessionUser, pageIndex, size, keyword);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/notice/notice";
    }

    // 관리자 공지 상세(관리자만)
    @GetMapping("/admin/notices/{noticeId}")
    public String adminDetail(@PathVariable("noticeId") Long noticeId,
                              HttpSession session,
                              Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.assertAdmin(sessionUser);

        model.addAttribute("notice", noticeService.findById(noticeId));
        return "admin/notice/detail";
    }

    // 관리자 작성 폼(관리자만)
    @GetMapping("/admin/notices/new")
    public String createNoticeForm(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.assertAdmin(sessionUser);

        return "admin/notice/create-form";
    }

    // 관리자 작성(관리자만)
    @PostMapping("/admin/notices/new")
    public String createNotice(HttpSession session,
                               @Valid NoticeRequest.CreateDTO request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.saveAsAdmin(request, sessionUser);

        return "redirect:/admin/notices";
    }

    // 관리자 수정 폼(관리자만)
    @GetMapping("/admin/notices/{noticeId}/edit")
    public String updateNoticeForm(@PathVariable("noticeId") Long noticeId,
                                   HttpSession session,
                                   Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.assertAdmin(sessionUser);

        model.addAttribute("notice", noticeService.findUpdateForm(noticeId));
        return "admin/notice/update-form";
    }

    // 관리자 수정(관리자만)
    @PostMapping("/admin/notices/{noticeId}/edit")
    public String updateNotice(@PathVariable("noticeId") Long noticeId,
                               NoticeRequest.UpdateDTO request,
                               HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.updateAsAdmin(noticeId, request, sessionUser);

        return "redirect:/admin/notices/" + noticeId;
    }

    // 관리자 삭제(관리자만)
    @PostMapping("/admin/notices/{noticeId}/delete")
    public String deleteNotice(@PathVariable("noticeId") Long noticeId,
                               HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.deleteAsAdmin(noticeId, sessionUser);

        return "redirect:/admin/notices";
    }
}
