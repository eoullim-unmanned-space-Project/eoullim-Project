package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.MailService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final MailService mailService;

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
                         HttpSession session, Model model,
                         RedirectAttributes redirectAttributes
    ) {
        request.validate();

        if (request.getIsEmailVerified() == null || !request.getIsEmailVerified()) {
            model.addAttribute("signupError", ErrorCode.INVALID_VERIFICATION_CODE);
            return "user/signup";
        }

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
                        HttpSession session, Model model
    ) {
        request.validate();

        try {
            User sessionUser = authService.login(request);

            session.setAttribute("sessionUser", sessionUser);

            return "redirect:/";

        } catch (Exception403 e) {
            String defaultMsg = e.getMessage();
            String reason = e.getReason();

            String loginErrorMsg;
            if (reason != null && !reason.isEmpty()) {
                loginErrorMsg = defaultMsg + "\n사유: " + reason;
            } else {
                loginErrorMsg = defaultMsg;
            }

            model.addAttribute("loginError", loginErrorMsg);

            return "user/login";

        } catch (Exception400 | Exception401 | Exception404 e) {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "user/login";
        }
    }

    /**
     * 로그아웃
     */
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/auth/login";
    }

    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "user/find-password";
    }

    @PostMapping("/find-password/send-code")
    public ResponseEntity<?> sendVerifiedCode(@RequestParam String userId,
                               @RequestParam String email
    ) {
        User user = userService.findByUserIdAndEmail(userId, email);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "아이디와 이메일이 일치하지 않습니다."));
        }
        mailService.sendVerificationCode(email);

        return ResponseEntity.ok(Map.of("message", "인증번호가 발송되었습니다."));
    }

    @PostMapping("/password/verify-code")
    @ResponseBody
    public ResponseEntity<?> verifyPasswordCode(@RequestParam String email,
                                     @RequestParam String code,
                                     @RequestParam String userId,
                                     HttpSession session
    ) {
        boolean verified = mailService.verifyVerificationCode(email, code);

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "인증번호가 일치하지 않습니다."));
        }

        session.setAttribute("passwordResetVerified", true);
        session.setAttribute("passwordUserId", userId);

        return ResponseEntity.ok(Map.of("message", "인증이 완료되었습니다."));
    }

    @GetMapping("/password/reset")
    public String resetPasswordForm(HttpSession session) {

        Boolean verified = (Boolean) session.getAttribute("passwordResetVerified");
        if (verified == null || !verified) {
            return "redirect:/auth/find-password";
        }
        return "user/reset-password";
    }

    @PostMapping("/password/reset")
    public String resetPassword(@RequestParam String newPassword,
                                HttpSession session,
                                Model model
    ) {
        Boolean verified = (Boolean) session.getAttribute("passwordResetVerified");
        String userId = (String) session.getAttribute("passwordUserId");

        if (verified == null || !verified || userId == null) {
            model.addAttribute("error", "잘못된 접근입니다.");
            return "user/find-password";
        }

        userService.updatePassword(userId, newPassword);

        session.removeAttribute("passwordResetVerified");
        session.removeAttribute("passwordUserId");

        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");

        return "redirect:/auth/login";
    }
}
