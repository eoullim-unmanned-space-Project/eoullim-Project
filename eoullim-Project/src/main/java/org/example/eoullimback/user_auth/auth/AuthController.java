package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;

import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.MailService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("permitAll()")
    public String signup(@ModelAttribute AuthRequest.SignupRequestDTO request,
                         Model model
    ) {
        request.validate();

        if (request.getIsEmailVerified() == null || !request.getIsEmailVerified()) {
            model.addAttribute("signupError", ErrorCode.INVALID_VERIFICATION_CODE);
            return "user/signup";
        }

        authService.signup(request);

        return  "redirect:/auth/login";
    }

    @GetMapping("/signup-check-login-id")
    @ResponseBody
    @PreAuthorize("permitAll()")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        boolean exists = userService.existsByLoginId(loginId);
        return ResponseEntity.ok(exists);
    }

    // http://localhost:8080/auth/login
    @GetMapping("/login")
    @PreAuthorize("permitAll()")
    public String loginForm() {
        return "user/login";
    }

    @GetMapping("/find-password")
    @PreAuthorize("permitAll()")
    public String findPasswordForm() {
        return "user/find-password";
    }

    @PostMapping("/find-password/send-code")
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("permitAll()")
    public String resetPasswordForm(HttpSession session) {

        Boolean verified = (Boolean) session.getAttribute("passwordResetVerified");
        if (verified == null || !verified) {
            return "redirect:/auth/find-password";
        }
        return "user/reset-password";
    }

    @PostMapping("/password/reset")
    @PreAuthorize("permitAll()")
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
