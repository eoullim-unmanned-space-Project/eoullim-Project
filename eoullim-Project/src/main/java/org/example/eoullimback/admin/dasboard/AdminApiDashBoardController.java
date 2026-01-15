package org.example.eoullimback.admin.dasboard;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment.PaymentResponse;
import org.example.eoullimback.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminApiDashBoardController {

    private final PaymentService paymentService;

    @GetMapping("/api/admin/payments")
    ResponseEntity<List<PaymentResponse.PaymentListDTO>> getAllPaymentList() {

        List<PaymentResponse.PaymentListDTO> payments = paymentService.getAllPaymentList();

        return ResponseEntity.ok().body(payments);
    }

}
