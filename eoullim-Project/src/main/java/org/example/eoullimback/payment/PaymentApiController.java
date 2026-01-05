package org.example.eoullimback.payment;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/api/payment/prepare")
    public ResponseEntity<?> prepare( @RequestBody PaymentRequest.PrepareDTO requestDTO, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다"));
        }

        PaymentResponse.PrepareDTO prepareDTO = paymentService.prepare(sessionUser.getId(), requestDTO.bookingCode);

        return ResponseEntity.ok().body(
                Map.of("paymentId", prepareDTO.getPaymentId(),"totalAmount", prepareDTO.getTotalAmount()));
    }


    @PostMapping("/api/payment/complete")
    public ResponseEntity<?> complete(@RequestBody PaymentRequest.CompleteDTO requestDTO, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다"));
        }

        log.info("결제 완료 요청 수신: impUid={}, merchantUid={}",
                requestDTO.getImpUid(), requestDTO.getMerchantUid());

        paymentService.complete(sessionUser.getId(), requestDTO.getImpUid(), requestDTO.getMerchantUid());

        return ResponseEntity.ok().body(Map.of("message", "결제를 완료했습니다. 이메일로 QR코드를 확인해주세요"));
    }

}
