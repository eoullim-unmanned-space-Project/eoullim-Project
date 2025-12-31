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

    private final NoticeServiceImpl noticeService;

    // 공지사항 작성 화면 요청
    // http://localhost:8080/notices/new
    @GetMapping("/notices/new")
    public String createNoticeForm() {
        return "notice/create-form";
    }

    // 공지사항 작성 요청 기능
    // http://localhost:8080/notices/new
    @PostMapping("/notices/new")
    public String createNotice(
            HttpSession session,
            @Valid NoticeRequest.CreateDTO request
    ) {
//        User sessionUser = (User) session.getAttribute("sessionSUser");
        User sessionUser = getLoginUserId(session);
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

        PageResponse.PageDTO<Notice, NoticeResponse.ListDTO> noticePage = noticeService.noticeListFindAll(pageIndex, size, keyword);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword != null ? keyword: "");

        return "notice/list";
    }

    // 공지사항 상세 보기 화면 요청
    // http://localhost:8080/notices/{id}
    @GetMapping("/notices/{id}")
    public String detailNotice(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("notice", noticeService.findById(id));
        return "notice/detail";
    }

    // 공지사항 수정 화면 요청
    // http://localhost:8080/notices
    @GetMapping("/notices/{id}/update")
    public String updateNoticeForm(@PathVariable Long id,
                                 Model model
    ) {
        model.addAttribute("notice", noticeService.findUpdateForm(id));
        return "notice/update-form";
    }

    // 공지사항 수정 요청 기능
    // http://localhost:8080/notices/{id}
    @PostMapping("/notices/{id}/update")
    public String updateNotice(@PathVariable Long id,
                               NoticeRequest.UpdateDTO request,
                               HttpSession session
    ) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);
        noticeService.update(id, request, sessionUser);
        return "redirect:/notices/{id}";
    }

    // 공지사항 삭제 요청 기능
    // http://localhost:8080/notices/{id}/delete
    @PostMapping("/notices/{id}/delete")
    public String deleteNotice(@PathVariable Long id,
                               HttpSession session
    ) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
        User sessionUser = getLoginUserId(session);
        noticeService.delete(id, sessionUser);
        return "redirect:/notices";
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
