package org.example.eoullimback.user_auth.auth;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.auth.dto.AuthRequest;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new AuthRequest.SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute AuthRequest.SignupRequest request) {
        authService.signup(request);

        return  "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new AuthRequest.LoginRequest());

        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AuthRequest.LoginRequest request, Model model) {
        User user = authService.login(request);
        model.addAttribute("user", user);

        return "redirect:/users/me?userId" + user.getId();
    }

    @PostMapping("logout")
    public String logout(@RequestParam("userId") Long userId) {
        authService.logout(userId);

        return "redirect:/auth/login";
    }
}
