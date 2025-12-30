package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // http://localhost:8080/auth/signup
    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup";
    }

    /**
     * 회원가입기능
     * @param request
     * @return
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Valid AuthRequest.SignupRequestDTO request,
                         HttpSession session
    ) {
        User newUser = authService.signup(request);

        session.setAttribute("sessionUser", newUser);

        return  "redirect:/user/profile";
    }

    @GetMapping("/signup-check-login-id")
    @ResponseBody
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        boolean exists = userService.existsByLoginId(loginId);
        return ResponseEntity.ok(exists);
    }

    // http://localhost:8080/auth/login
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    /**
     * 로그인 기능
     */
    @PostMapping("/login")
    public String login(@ModelAttribute @Valid AuthRequest.LoginRequestDTO request,
                        HttpSession session
    ) {
        User sessionUser = authService.login(request);

        session.setAttribute("sessionUser", sessionUser);

        return "redirect:/main/main";
    }

    /**
     * 로그아웃
     */
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/auth/login";
    }
}
