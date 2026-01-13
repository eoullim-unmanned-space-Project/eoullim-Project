package org.example.eoullimback.comment;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentServiceImpl commentService;

    // 댓글 작성
    @PostMapping("/qaa/{qaaId}/comments/new")
    public String createComment(
            CommentRequest.createDTO saveRequest,
            HttpSession session
    ) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        commentService.createComment(saveRequest, sessionUser.getId());

        return "redirect:/qaas/" + saveRequest.getQaaId();
    }

    //수정 화면
    @GetMapping("/comments/{id}/update")
    public String updateForm(@PathVariable Long id,
                             HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Long qaaId = commentService.getQaaIdByCommentId(id);

        session.setAttribute("commentId", id);

        return "redirect:/qaas/" + qaaId;
    }

    // 수정 요청 기능
    @PostMapping("/comments/{id}/update")
    public String updateComment(@PathVariable Long id,
                                CommentRequest.UpdateDTO request,
                                HttpSession session
    ) {
        User sessionUser =  (User)session.getAttribute("sessionUser");
        Long qaaId = commentService.getQaaIdByCommentId(id);

        commentService.updateComment(request, id, sessionUser.getId());

        session.removeAttribute("commentId");

        return "redirect:/qaas/" + qaaId;
    }

    // 삭제 요청 기능
    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable(name = "id") Long commentId,
                                HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Long qaaId = commentService.deleteComment(commentId, sessionUser.getId());

        return "redirect:/qaas/" + qaaId;
    }
}
