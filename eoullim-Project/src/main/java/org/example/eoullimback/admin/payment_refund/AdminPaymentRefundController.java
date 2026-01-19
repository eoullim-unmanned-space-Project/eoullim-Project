package org.example.eoullimback.admin.payment_refund;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AdminPaymentRefundController {

    private final PaymentRefundService paymentRefundService;

    @GetMapping("/admin/refunds/{id}/detail")
    public ResponseEntity<PaymentRefundResponse.AdminDetailDTO> detail(@PathVariable Long id) {

        PaymentRefundResponse.AdminDetailDTO detailDTO = paymentRefundService.detail(id);

        return ResponseEntity.ok().body(detailDTO);
    }

}
