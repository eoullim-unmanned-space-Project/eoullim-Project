package org.example.eoullimback.user_auth.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // // http://localhost:8080/users/me
    // 유저 컨트롤러
    @GetMapping("/me/{id}")
    public String myProfile(@RequestParam("userId") Long userId, Model model) {

        User user = userService.getMyProfile(userId);

        model.addAttribute("user", user);

        return "users/profile";
    }

    /**
     * 회원 수정(프로필 생성)
     */
    @PostMapping("/me/{id}")
    public String updateProfile(@RequestParam("userId") Long id, @ModelAttribute @Valid UserRequest.UpDateDTO update) {

        userService.updateProfile(id, update);

        return "redirect:/users/me?userId="+ id;
    }

    /**
     * 회원 수정(프로필 삭제)
     */
    @PostMapping("/profile-image/delete")
    public String deleteProfileImage(@RequestParam("userId") Long id) {

        userService.deleteProfileImage(id);

        return "redirect:/users/me";
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/leave")
    public String leaveUser(@RequestParam("userId") Long id) {

        userService.leaveUser(id);

        return "redirect:/logout";
    }
}
