package org.example.eoullimback.user_auth.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // // http://localhost:8080/users/profile/1
    // 유저 컨트롤러 , @PathVariable Long id,
    @GetMapping("/profile")
    public String myProfile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.getMyProfile(sessionUser.getId());

        model.addAttribute("user", user);

        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.getMyProfile(sessionUser.getId());

//        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("user", user);

        return "user/update-profile";
    }

    /**
     * 회원 수정(프로필)
     */
    // // http://localhost:8080/users/profile/1
    @PostMapping("/profile")
    public String updateProfile(HttpSession session,
                                @ModelAttribute @Valid UserRequest.UpDateDTO update
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User updateUser = userService.updateProfile(sessionUser.getId(), update);

        session.setAttribute("sessionUser", updateUser);

        return "redirect:/user/profile";
    }

    /**
     * 회원 수정(프로필 이미지 삭제)
     */
    // // http://localhost:8080/users/profile-delete/1
    @PostMapping("/profile-delete")
    public String deleteProfileImage(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.deleteProfileImage(sessionUser.getId());

        User updateUser = userService.getMyProfile(sessionUser.getId());
        session.setAttribute("sessionUser", updateUser);

        return "redirect:/user/profile";
    }

    /**
     * 회원 탈퇴
     */
    // // http://localhost:8080/users/leave/1
    @PostMapping("/leave")
    public String leaveUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.leaveUser(sessionUser.getId());

        session.invalidate();
        return "redirect:/auth/login";
    }
}
