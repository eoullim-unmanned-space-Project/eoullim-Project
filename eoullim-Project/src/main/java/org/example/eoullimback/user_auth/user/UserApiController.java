package org.example.eoullimback.user_auth.user;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/api/email/send")
    public ResponseEntity<?> sendVerificationCode(@RequestBody UserRequest.EmailCheckDTO reqDTO) {

        reqDTO.validate();

        mailService.sendVerificationCode(reqDTO.getEmail());

        return ResponseEntity.ok().body(Map.of("message", "인증번호가 발송되었습니다."));
    }

    @PostMapping("/api/email/verify")
    public ResponseEntity<?> verifyEmailVerificationCode(@RequestBody UserRequest.EmailCheckDTO reqDTO) {

        reqDTO.validate();
        if (reqDTO.getCode() == null || reqDTO.getCode().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "인증번호를 입력해주세요."));
        }

        boolean isVerified = mailService.verifyVerificationCode(reqDTO.getEmail(), reqDTO.getCode());
        if (isVerified) {
            return ResponseEntity
                    .ok()
                    .body(Map.of("message", "인증되었습니다."));
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "인증번호가 일치하지 않습니다."));
        }
    }
}
