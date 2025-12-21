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
    @GetMapping("/me")
    public String myProfile(@RequestParam("userId") Long userId, Model model) {

        User user = userService.getMyProfile(userId);

        model.addAttribute("user", user);

        return "user/profile";
    }

    /**
     * 회원 수정(프로필 생성)
     */
    @PostMapping("/me")
    public String updateProfile(@RequestParam("userId") Long userId, @ModelAttribute @Valid UserRequest.UpDateDTO upDateDTO, Model model) {

        userService.updateProfile(userId, upDateDTO);

        return "redirect:/users/me?userId="+ userId;
    }

    /**
     * 회원 수정(프로필 삭제)
     */
    @PostMapping("/profile-image/delete")
    public String deleteProfileImage(@RequestParam("userId") Long userId) {

        userService.deleteProfileImage(userId);

        return "redirect:/user/me";
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/leave")
    public String leaveUser(@RequestParam("userId") Long userId) {

        userService.leaveUser(userId);

        return "redirect:/logout";
    }
}
