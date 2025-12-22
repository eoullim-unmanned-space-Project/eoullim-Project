package org.example.eoullimback.user_auth.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // http://localhost:8080/auth/signup
    @GetMapping("/signup")
    public String signupForm() {
        return "/user/signup";
    }

    /**
     * 회원가입기능
     * @param requestDTO
     * @return
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Valid AuthRequest.SignupRequest requestDTO) {
        authService.signup(requestDTO);

        return  "redirect:/auth/login";
    }

    // http://localhost:8080/auth/login
    @GetMapping("/login")
    public String loginForm() {
        return "/user/login";
    }

    /**
     * 로그인 기능
     */
    @PostMapping("/login")
    public String login(@ModelAttribute @Valid AuthRequest.LoginRequest requestDTO) {
        System.out.println("111111111111111111111111111111111111111");
        User user = authService.login(requestDTO);

        return "redirect:/main/main";
    }

    /**
     * 추후 구현
     */
//    @PostMapping("logout")
//    public String logout(@RequestParam("userId") Long userId) {
//        authService.logout(userId);
//
//        return "redirect:/auth/login";
//    }
}
