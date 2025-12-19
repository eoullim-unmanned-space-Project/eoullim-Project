package org.example.eoullimback.notice;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageDTO;
import org.example.eoullimback.notice.dto.request.NoticeSaveRequest;
import org.example.eoullimback.notice.dto.request.NoticeUpdateRequest;
import org.example.eoullimback.notice.dto.response.NoticeListResponse;
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

    // 공지사항 화면 요청
    // http://localhost:8080/notices/new
    @GetMapping("/notices/new")
    public String createNoticeForm(
            HttpSession session,
            @Valid NoticeSaveRequest request
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        noticeService.save(request, sessionUser);

        return "redirect:/notices";
    }

    // 공지사항 작성 요청 기능
    // http://localhost:8080/notices
    @PostMapping("/notices")
    public String createNotice(
            HttpSession session,
            @Valid NoticeSaveRequest request
    ) {
        User sessionUser = (User) session.getAttribute("sessionSUser");
        noticeService.save(request, sessionUser);

        return "redirect:/notices";
    }

    // 공지사항 목록 화면 요청
    // http://localhost:8080/notices
    @GetMapping("/notices")
    public String listNotice(Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);
        PageDTO<NoticeListResponse> noticePage = noticeService.noticeListFindAll(pageIndex, size, keyword);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword != null ? keyword: "");

        return "notices/list";
    }

    // 공지사항 상세 보기 화면 요청
    // http://localhost:8080/notices/{id}
    @GetMapping("/notices/{id}")
    public String detailNoticeForm(@PathVariable Long id,
                                   Model model
    ) {
        model.addAttribute("notice", noticeService.findById(id));
        return "notices/detail";
    }

    // 공지사항 수정 화면 요청
    // http://localhost:8080/notices
    @PostMapping("/notices/{id}/edit")
    public String editNoticeForm(@PathVariable Long id,
                                 Model model
    ) {
        model.addAttribute("notice", noticeService.findUpdateForm(id));
        return "notices/update-form";
    }

    // 공지사항 수정 요청 기능
    // http://localhost:8080/notices/{id}
    @PostMapping("/notices/{id}")
    public String updateNotice(@PathVariable Long id,
                               NoticeUpdateRequest updateRequest,
                               HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        noticeService.update(id, updateRequest, sessionUser);
        return "redirect:/notices/{id}";
    }

    // 공지사항 삭제 요청 기능
    // http://localhost:8080/notices/{id}/delete
    @PostMapping("/notices/{id}/delete")
    public String deleteNotice(@PathVariable Long id,
                               HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        noticeService.delete(id, sessionUser);
        return "redirect:/notices";
    }
}
