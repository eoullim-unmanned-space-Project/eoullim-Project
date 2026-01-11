package org.example.eoullimback.user_auth;

import jakarta.servlet.http.HttpSession;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping("/main")
    public String index(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        return "/main/main";
    }
}
