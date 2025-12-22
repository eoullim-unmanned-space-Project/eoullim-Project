package org.example.eoullimback.comment;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentServiceImpl commentService;

    // 댓글 작성
    @PostMapping("comments/new")
    public String createComment(CommentRequest.CreateDTO saveRequest, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        commentService.createComment(saveRequest, sessionUser.getId());

        return "redirect:/qaa/" + saveRequest.getQaaId();
    }

    //수정 화면 요청
    @GetMapping("/comments/{id}/update")
    public String updateForm(@PathVariable Long id,
                             Model model,
                             HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Long qaaId = commentService.deleteComment(id, sessionUser.getId());

        CommentResponse.UpdateFormDTO comment = commentService.updateCommentForm(id, sessionUser.getId());

        model.addAttribute("comment", comment);

        return "redirect:/qaa/" + qaaId;
    }

    // 수정 요청 기능
    @PostMapping("/comments/{id}/update")
    public String updateComment(@PathVariable Long id,
                                CommentRequest.UpdateDTO request,
                                HttpSession session
    ) {
        User sessionUser =  (User)session.getAttribute("sessionUser");
        Long qaaId = commentService.deleteComment(id, sessionUser.getId());

        commentService.updateComment(request, id, sessionUser.getId());

        return "redirect:/qaa/" + qaaId;
    }

    @PostMapping("comments/{id}/delete")
    public String deleteComment(@PathVariable(name = "id") Long commentId,
                                HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        Long qaaId = commentService.deleteComment(commentId, sessionUser.getId());

        return "redirect:/qaa/" + qaaId;
    }


}
