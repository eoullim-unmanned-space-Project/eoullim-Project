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

    // // http://localhost:8080/users/profile/1
    // 유저 컨트롤러 , @PathVariable Long id,
    @GetMapping("/profile/{id}")
    public String myProfile(@PathVariable Long id, Model model) {

        User user = userService.getMyProfile(id);

        model.addAttribute("user", user);

        return "user/profile";
    }

    /**
     * 회원 수정(프로필 생성)
     */
    // // http://localhost:8080/users/profile/1
    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable Long id, @ModelAttribute @Valid UserRequest.UpDateDTO update) {

        userService.updateProfile(id, update);

        return "redirect:users/me/"+ id;
    }

    /**
     * 회원 수정(프로필 삭제)
     */
    // // http://localhost:8080/users/profile-delete/1
    @PostMapping("/profile-delete/{id}")
    public String deleteProfileImage(@PathVariable Long id) {

        userService.deleteProfileImage(id);

        return "redirect:user/me";
    }

    /**
     * 회원 탈퇴
     */
    // // http://localhost:8080/users/leave/1
    @PostMapping("/leave/{id}")
    public String leaveUser(@PathVariable Long id) {

        userService.leaveUser(id);

        return "redirect:/logout";
    }
}
