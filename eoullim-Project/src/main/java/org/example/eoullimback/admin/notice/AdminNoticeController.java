package org.example.eoullimback.admin.notice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.notice.Notice;
import org.example.eoullimback.notice.NoticeRequest;
import org.example.eoullimback.notice.NoticeResponse;
import org.example.eoullimback.notice.NoticeService;
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

@Controller
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    //  관리자 공지 목록(관리자만)
    @GetMapping("/admin/notices")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminListNotice(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(required = false) String keyword) {

        User user = userDetails.getUser();
        noticeService.assertAdmin(user);

        int pageIndex = Math.max(0, page - 1);

        PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticePage =
                noticeService.adminNoticeListFindAll(user, pageIndex, size, keyword);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "admin/notice/notice";
    }

    // 관리자 공지 상세(관리자만)
    @GetMapping("/admin/notices/{noticeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDetail(@PathVariable("noticeId") Long noticeId,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        User user = userDetails.getUser();
        noticeService.assertAdmin(user);

        model.addAttribute("notice", noticeService.findById(noticeId));
        return "admin/notice/detail";
    }

    // 관리자 작성 폼(관리자만)
    @GetMapping("/admin/notices/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createNoticeForm(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        noticeService.assertAdmin(user);

        return "admin/notice/create-form";
    }

    // 관리자 작성(관리자만)
    @PostMapping("/admin/notices/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createNotice(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @Valid NoticeRequest.CreateDTO request) {
        User user = userDetails.getUser();
        noticeService.saveAsAdmin(request, user);

        return "redirect:/admin/notices";
    }

    // 관리자 수정 폼(관리자만)
    @GetMapping("/admin/notices/{noticeId}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateNoticeForm(@PathVariable("noticeId") Long noticeId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {
        User user = userDetails.getUser();
        noticeService.assertAdmin(user);

        model.addAttribute("notice", noticeService.findUpdateForm(noticeId));
        return "admin/notice/update-form";
    }

    // 관리자 수정(관리자만)
    @PostMapping("/admin/notices/{noticeId}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateNotice(@PathVariable("noticeId") Long noticeId,
                               NoticeRequest.UpdateDTO request,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        noticeService.updateAsAdmin(noticeId, request, user);

        return "redirect:/admin/notices/" + noticeId;
    }

    // 관리자 삭제(관리자만)
    @PostMapping("/admin/notices/{noticeId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteNotice(@PathVariable("noticeId") Long noticeId,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        noticeService.deleteAsAdmin(noticeId, user);

        return "redirect:/admin/notices";
    }
}
