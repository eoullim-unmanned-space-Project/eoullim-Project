package org.example.eoullimback.payment;

import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/api/user/payments/prepare")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> prepare(
            @RequestBody PaymentRequest.PrepareDTO requestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {

        User user = userDetails.getUser();

        PaymentResponse.PrepareDTO prepareDTO = paymentService.prepare(user.getId(), requestDTO.bookingCode);

        return ResponseEntity.ok().body(
                Map.of("paymentKey", prepareDTO.getPaymentKey(),"totalAmount", prepareDTO.getTotalAmount()));
    }


    @PostMapping("/api/user/payments/complete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> complete(@RequestBody PaymentRequest.CompleteDTO requestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException {

        User user = userDetails.getUser();

        log.info("결제 완료 요청 수신: impUid={}, merchantUid={}",
                requestDTO.getImpUid(), requestDTO.getMerchantUid());

        String bookingCode = paymentService.complete(user.getId(), requestDTO.getImpUid(), requestDTO.getMerchantUid());

        if (bookingCode != null) {
            return ResponseEntity.ok().body(Map.of("message", "결제를 완료했습니다. 이메일로 QR코드를 확인해주세요", "bookingCode", bookingCode, "success", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "결제가 취소되었습니다."));
        }
    }

    @PostMapping("/api/user/payments/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancel(@RequestBody PaymentRequest.CancelDTO cancelDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        log.info("결제 취소 요청 수신: merchantUid={}", cancelDTO.merchantUid);

        paymentService.cancel(cancelDTO.merchantUid);

        return ResponseEntity.ok().body(Map.of("message", "결제를 취소했습니다."));
    }

}
