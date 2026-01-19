package org.example.eoullimback.admin.payment_refund;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminApiPaymentRefundController {

    private final PaymentRefundService paymentRefundService;

    @PostMapping("/api/admin/refunds/{id}/rejection")
    public ResponseEntity<Void> rejection(@PathVariable Long id, @RequestBody PaymentRefundRequest.RejectionDTO rejectionDTO) {

        paymentRefundService.rejection(id, rejectionDTO.getReason());

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/api/admin/refunds/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {

        paymentRefundService.approve(id);

        return ResponseEntity.ok().body(null);
    }
}
