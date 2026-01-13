package org.example.eoullimback.review;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ReviewPageController {

    @GetMapping("/user/reviews")
    public String page(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        model.addAttribute("user", sessionUser);

        return "user/review";
    }
}
