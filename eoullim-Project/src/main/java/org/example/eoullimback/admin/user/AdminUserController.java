package org.example.eoullimback.admin.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/users")
    public String users(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        List<UserResponse.AdminListDTO> userList = userService.getUserList();
        model.addAttribute("adminUserList", userList);

        return "admin/user";
    }

}
