package org.example.eoullimback.notification;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.user_auth.user.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final MailService mailService;
    private final QrCodeGenerator qrCodeGenerator;

    @GetMapping("/dev/mail/test")
    public String sendTestMail() {

        Payment payment = Payment.builder()
                .orderId("TEST-ORDER-1234")
                .amount(50000L)
                .productName("테스트 숙소")
                .method(PaymentMethod.MOCK)
                .status(PaymentStatus.SUCCESS)
                .build();

        byte[] qrImage = qrCodeGenerator.generate(
                "http://localhost:8080/notifications"
        );

        mailService.sendPaymentSuccessMail(
                "jyp10311@naver.com",
                payment,
                qrImage
        );

        return "메일 + QR 코드 전송 테스트 완료";
    }
}
