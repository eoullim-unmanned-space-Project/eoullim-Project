package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/auth")
public class AuthApiController {

    private final UserService userService;
    private final MailService mailService;

    // 비밀번호 찾기 인증 코드 발송
    @PostMapping("/password-reset/code")
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

    // 비밀번호 찾기 인증번호 확인
    @PostMapping("/password-reset/verify") //
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

    // 비밀번호 변경
    @PutMapping("/password-reset")
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
