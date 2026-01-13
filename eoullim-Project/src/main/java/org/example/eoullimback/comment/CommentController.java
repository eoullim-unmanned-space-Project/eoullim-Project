package org.example.eoullimback.comment;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/admin/qna/{qaaId}/comments/new")
    public String createAdminComment(@PathVariable Long qaaId,
                                     CommentRequest.createDTO saveRequest,
                                     HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        saveRequest.setQaaId(qaaId);
        commentService.createCommentAsAdmin(saveRequest, sessionUser.getId());

        return "redirect:/admin/qna/" + qaaId;
    }

    @PostMapping("/admin/comments/{id}/delete")
    public String adminDeleteComment(@PathVariable Long id, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception403(ErrorCode.LOGIN_ONLY);

        Long qaaId = commentService.deleteCommentAsAdmin(id, sessionUser.getId());

        return "redirect:/admin/qna/" + qaaId;
    }
}
