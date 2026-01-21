package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.MailService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
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

    /**
     * 아이디 찾기 - 이메일 전송
     */
    @PostMapping("/find-id/code")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> sendVerificationCode(
            @RequestBody UserRequest.EmailCheckDTO reqDTO
    ) {
        reqDTO.validate();

        mailService.sendVerificationCode(reqDTO.getEmail());

        return ResponseEntity.ok().body(Map.of("message", "인증번호가 발송되었습니다."));
    }

    @PostMapping("/find-id/verify")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> verifyEmailVerificationCode(
            @RequestBody UserRequest.EmailCheckDTO reqDTO,
            HttpSession session
    ) {

        reqDTO.validate();

        if (reqDTO.getCode() == null || reqDTO.getCode().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "인증번호를 입력해주세요."));
        }

        boolean isVerified = mailService.verifyVerificationCode(reqDTO.getEmail(), reqDTO.getCode());

        if (isVerified) {
            session.setAttribute("findIdVerified", true);
            session.setAttribute("findIdEmail", reqDTO.getEmail());

            return ResponseEntity
                    .ok()
                    .body(Map.of("message", "인증되었습니다."));
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "인증번호가 일치하지 않습니다."));
        }
    }

    @PostMapping("/find-id")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> findLoginId(
            HttpSession session
    ) {
        Boolean verified =
                (Boolean)  session.getAttribute("findIdVerified");

        String email =
                (String)   session.getAttribute("findIdEmail");

        if (verified == null || !verified) {
            throw new Exception401(ErrorCode.MISSING_EMAIL);
        }

        User user = userService.findByEmail(email);

        session.removeAttribute("findIdVerified");
        session.removeAttribute("findIdEmail");

        return ResponseEntity.ok(user.getLoginId());
    }

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

    @PutMapping("/password-reset")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body,
                                           HttpSession session) {
        Boolean verified = (Boolean) session.getAttribute("passwordResetVerified");
        String userId = (String) session.getAttribute("passwordUserId");

        if (verified == null || !verified || userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("code", "LOGIN_UNAUTHORIZED", "message", ErrorCode.LOGIN_UNAUTHORIZED.getMessage()));
        }

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("code", "PASSWORD_REQUIRED", "message", ErrorCode.PASSWORD_REQUIRED.getMessage()));
        }

        userService.updatePassword(userId, newPassword);

        session.removeAttribute("passwordResetVerified");
        session.removeAttribute("passwordUserId");

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }

}
