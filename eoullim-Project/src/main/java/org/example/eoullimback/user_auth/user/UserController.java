package org.example.eoullimback.user_auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 유저 컨트롤러
    @GetMapping("/me")
    public String myProfile(@RequestParam("userId") Long userId, Model model) {
        model.addAttribute("user", userService.getMyProfile(userId));

        return "profile";
    }

    @PatchMapping("/me")
    public String updateMyProfile(@RequestParam("userId") Long userId,
                                  @ModelAttribute UserRequest.UpdateProfileRequest request) {
        userService.updateMyProfile(userId, request);

        return "redirect:/users/me?userId=" + userId;
    }

    @DeleteMapping("/me")
    public String deleteMyProfile(@RequestParam("userId") Long userId) {
        userService.deleteMyProfile(userId);
        return "redirect:/";
    }

    // 관리자 컨트롤러
    @GetMapping("/all")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "user-list";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));

        return "user-detail";
    }

    @PutMapping("/{id}/role")
    public String updateRole(@PathVariable("id") Long id,
                             @ModelAttribute UserRequest.UpdateRoleRequest request) {
        userService.updateUserRole(id, request);
        return "redirect:/users/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return "redirect:/users";
    }
}
