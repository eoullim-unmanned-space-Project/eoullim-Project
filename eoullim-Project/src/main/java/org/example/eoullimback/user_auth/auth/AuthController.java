package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String signup(@ModelAttribute AuthRequest.SignupRequestDTO request,
                         HttpSession session
    ) {
        request.validate();

        User newUser = authService.signup(request);

        session.setAttribute("sessionUser", newUser);

        return  "redirect:/auth/login";
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
    public String login(@ModelAttribute AuthRequest.LoginRequestDTO request,
                        HttpSession session
    ) {
        request.validate();

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

    @GetMapping("/find/Login-id")
    public String findLoginId() {
        return "user/find-loginId";
    }

    @PostMapping("/find/Login-id")
    public String findLoginId(HttpSession session,
                              Model model) {
        Boolean verified =
                (Boolean) session.getAttribute("findIdVerified");
        String email =
                (String) session.getAttribute("findIdEmail");

        if (verified == null || !verified) {
            throw new Exception401(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        User user = userService.findByEmail(email);

        if (user.isSocialUser()) {
            throw new Exception400(ErrorCode.SOCIAL_USER_CANNOT_FIND_LOGIN_ID);
        }

        session.removeAttribute("findIdVerified");
        session.removeAttribute("findIdEmail");

        model.addAttribute("loginId", user.getLoginId());

        return "user/find-loginId-result";
        }



    @GetMapping("/password/reset/request")
    public String requestPasswordResetForm() {
        return "user/password-reset-request";
    }

    @PostMapping("/password/reset/request")
    public String requestPasswordReset(AuthRequest.ResetPasswordRequestDTO request, HttpSession session) {

        authService.requestPasswordReset(request, session);

        return "user/password-reset";
    }

    @PostMapping("/password/reset/confirm")
    public String resetPassword(AuthRequest.ResetPasswordConfirmDTO request, HttpSession session) {

        authService.resetPassword(request, session);

        return "redirect:/auth/login";
    }
}
