package org.example.eoullimback.review.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminReviewPageController {

    @GetMapping("/admin/reviews")
    public String page(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);
//        if (!sessionUser.isAdmin()) throw new Exception409(ErrorCode.FORBIDDEN);

        return "admin/review";
    }
}
