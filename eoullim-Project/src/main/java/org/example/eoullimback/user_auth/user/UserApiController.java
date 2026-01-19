package org.example.eoullimback.user_auth.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.booking.BookingService;
import org.example.eoullimback.notification.NotificationService;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment.PaymentResponse;
import org.example.eoullimback.payment.PaymentService;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.user_auth.auth.AuthService;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/users/{userId}/email-verifications")
    public ResponseEntity<?> sendVerificationCode(@PathVariable Long userId,
                                                  @RequestBody UserRequest.EmailCheckDTO reqDTO
    ) {
        reqDTO.validate();

        User user = userService.findById(userId);
        mailService.sendVerificationCode(reqDTO.getEmail());

        return ResponseEntity.ok().body(Map.of("message", "인증번호가 발송되었습니다."));
    }

    @PostMapping("/users/{userId}/email-verifications/verify")
    public ResponseEntity<?> verifyEmailVerificationCode(@PathVariable Long userId,
                                                         @RequestBody UserRequest.EmailCheckDTO reqDTO) {

        reqDTO.validate();

        if (reqDTO.getCode() == null || reqDTO.getCode().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "인증번호를 입력해주세요."));
        }

        User user = userService.findById(userId);
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

    @PostMapping("/users/{userId}/password-verify")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody UserRequest.PasswordCheckDTO passwordCheckDTO, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        passwordCheckDTO.validate();

        authService.verifyPassword(sessionUser, passwordCheckDTO.getPassword());

        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/users/email-find-verifications")
    public ResponseEntity<?> sendFindEmail(
            @RequestBody UserRequest.EmailCheckDTO reqDTO
    ) {
        reqDTO.validate();

        mailService.sendVerificationCode(reqDTO.getEmail());

        return ResponseEntity.ok(
                Map.of("message", "인증번호 발송")
        );
    }

    @PostMapping("/users/email-find-verifications/verify")
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

    @PostMapping("/users/find-login-id")
    public ResponseEntity<String> findLoginId(HttpSession session
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

    @GetMapping("/api/user/search")
    public ResponseEntity<List<UserResponse.UserBookingDTO>> searchBookings(
            @RequestParam(value = "code", required = false) String bookingCode,
            @RequestParam(value = "status", required = false) BookingStatus status,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("sessionUser");

        if (user == null) {
            throw new Exception401(ErrorCode.ACCESS_DENIED);
        }

        List<UserResponse.UserBookingDTO> searchBookings = bookingService.searchBookings(user.getId(), bookingCode, status);

        return ResponseEntity.ok().body(searchBookings);
    }

    @GetMapping("/api/user/payment")
    public ResponseEntity<UserResponse.UserPaymentDTO> paymentDetail(
            @RequestParam(value = "code") String bookingCode,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new Exception401(ErrorCode.ACCESS_DENIED);
        }

        UserResponse.UserPaymentDTO userPaymentDTO = paymentService.paymentDetail(bookingCode, sessionUser.getId());

        return ResponseEntity.ok().body(userPaymentDTO);
    }

    @PostMapping("/api/user/refund")
    public ResponseEntity<Map<String, String>> createRefund(@RequestBody PaymentRefundRequest.CreateRefundDTO createRefundDTO, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        paymentRefundService.createRefund(createRefundDTO.getPaymentKey(), createRefundDTO.getReason(), sessionUser.getId());

        return ResponseEntity.ok().body(Map.of("message", "환불요청을 접수했습니다."));
    }

    @PostMapping("/api/user/use-qrCode/{id}")
    public ResponseEntity<?> useQrcode(HttpSession session, @PathVariable Long id) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        notificationService.useQrcode(sessionUser.getId(), id);
        return ResponseEntity.ok().body(null);
    }

}
