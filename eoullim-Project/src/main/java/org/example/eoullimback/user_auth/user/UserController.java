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
//    private final Long userId = 1L;

    // // http://localhost:8080/users/me/1
    // 유저 컨트롤러 , @PathVariable Long id,
    @GetMapping("/me/{id}")
    public String myProfile(@PathVariable Long id, Model model) {

        User user = userService.getMyProfile(id);

        model.addAttribute("user", user);

        return "user/profile";
    }

    /**
     * 회원 수정(프로필 생성)
     */
    @PostMapping("/me/{id}")
    public String updateProfile(@PathVariable Long id, @ModelAttribute @Valid UserRequest.UpDateDTO update) {

        userService.updateProfile(id, update);

        return "redirect:users/me/"+ id;
    }

    /**
     * 회원 수정(프로필 삭제)
     */
    @PostMapping("/profile-image/delete")
    public String deleteProfileImage(@PathVariable Long id) {

        userService.deleteProfileImage(id);

        return "redirect:user/me";
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/leave")
    public String leaveUser(@PathVariable Long id) {

        userService.leaveUser(id);

        return "redirect:/logout";
    }
}
