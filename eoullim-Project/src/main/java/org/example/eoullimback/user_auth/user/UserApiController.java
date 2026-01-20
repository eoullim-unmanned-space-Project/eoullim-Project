package org.example.eoullimback.user_auth.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.booking.BookingService;
import org.example.eoullimback.notification.NotificationService;
import org.example.eoullimback.payment.PaymentService;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.user_auth.auth.AuthService;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;
    private final MailService mailService;
    private final AuthService authService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final PaymentRefundService paymentRefundService;
    private final NotificationService notificationService;

    @PostMapping("/user/{userId}/email-verifications")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> sendVerificationCode(
            @PathVariable Long userId,
            @RequestBody UserRequest.EmailCheckDTO reqDTO
    ) {
        reqDTO.validate();

        userService.findById(userId);
        mailService.sendVerificationCode(reqDTO.getEmail());

        return ResponseEntity.ok().body(Map.of("message", "인증번호가 발송되었습니다."));
    }

    @PostMapping("/user/{userId}/email-verifications/verify")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> verifyEmailVerificationCode(
            @PathVariable Long userId,
            @RequestBody UserRequest.EmailCheckDTO reqDTO
    ) {

        reqDTO.validate();

        if (reqDTO.getCode() == null || reqDTO.getCode().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "인증번호를 입력해주세요."));
        }

        userService.findById(userId);
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

    @PostMapping("/user/password-verify")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Boolean> verifyPassword(
            @RequestBody UserRequest.PasswordCheckDTO passwordCheckDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
          ) {
        User user = userDetails.getUser();

        passwordCheckDTO.validate();

        boolean isValid = authService.verifyPassword(user.getId(), passwordCheckDTO.getPassword());

        return ResponseEntity.ok().body(isValid);
    }

    @PostMapping("/user/email-find-verifications")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> sendFindEmail(
            @RequestBody UserRequest.EmailCheckDTO reqDTO
    ) {
        reqDTO.validate();

        mailService.sendVerificationCode(reqDTO.getEmail());

        return ResponseEntity.ok(
                Map.of("message", "인증번호 발송")
        );
    }

    @PostMapping("/user/email-find-verifications/verify")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> verifyFindIdEmail(
            @RequestBody UserRequest.EmailCheckDTO reqDTO,
            HttpSession session
    ) {
        reqDTO.validate();

        boolean verified =
                mailService.verifyVerificationCode(reqDTO.getEmail(), reqDTO.getCode());

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "인증번호가 일치하지 않습니다."));
        }

        session.setAttribute("findIdVerified", true);
        session.setAttribute("findIdEmail", reqDTO.getEmail());

        return ResponseEntity.ok(
                Map.of("message", "이메일 인증 완료.")
        );
    }

    @PostMapping("/user/find-login-id")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
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

    @GetMapping("/user/search")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<UserResponse.UserBookingDTO>> searchBookings(
            @RequestParam(value = "code", required = false) String bookingCode,
            @RequestParam(value = "status", required = false) BookingStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        if (user == null) {
            throw new Exception401(ErrorCode.ACCESS_DENIED);
        }

        List<UserResponse.UserBookingDTO> searchBookings = bookingService.searchBookings(user.getId(), bookingCode, status);

        return ResponseEntity.ok().body(searchBookings);
    }

    @GetMapping("/user/payment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserResponse.UserPaymentDTO> paymentDetail(
            @RequestParam(value = "code") String bookingCode,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        if (user == null) {
            throw new Exception401(ErrorCode.ACCESS_DENIED);
        }

        UserResponse.UserPaymentDTO userPaymentDTO = paymentService.paymentDetail(bookingCode, user.getId());

        return ResponseEntity.ok().body(userPaymentDTO);
    }

    @PostMapping("/user/refund")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Map<String, String>> createRefund(
            @RequestBody PaymentRefundRequest.CreateRefundDTO createRefundDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        paymentRefundService.createRefund(createRefundDTO.getPaymentKey(), createRefundDTO.getReason(), user.getId());

        return ResponseEntity.ok().body(Map.of("message", "환불요청을 접수했습니다."));
    }

    @PostMapping("/user/use-qrCode/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> useQrcode(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
        ) {

        User user = userDetails.getUser();

        notificationService.useQrcode(user.getId(), id);
        return ResponseEntity.ok().body(null);
    }

}
