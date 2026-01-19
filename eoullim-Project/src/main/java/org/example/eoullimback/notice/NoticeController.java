package org.example.eoullimback.notice;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 목록 화면 요청
    // http://localhost:8080/notices
    @GetMapping("/notices")
    public String listNotice(Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(required = false) String keyword) {
        int pageIndex = Math.max(0, page - 1);

        PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticePage =
                noticeService.noticeListFindAll(pageIndex, size, keyword);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "notice/list";
    }

    // 공지사항 상세 보기 화면 요청
    // http://localhost:8080/notices/{id}
    @GetMapping("/notices/{noticeId}")
    public String detailNotice(@PathVariable("noticeId") Long noticeId,
                               Model model) {
        model.addAttribute("notice", noticeService.findById(noticeId));
        return "notice/detail";
    }
}
