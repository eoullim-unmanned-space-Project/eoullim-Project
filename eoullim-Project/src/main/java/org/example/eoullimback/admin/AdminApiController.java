package org.example.eoullimback.admin;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final PaymentRefundService paymentRefundService;

    @GetMapping("/admin/refund/detail/{id}")
    public ResponseEntity<PaymentRefundResponse.AdminDetailDTO> detail(@PathVariable Long id) {

        PaymentRefundResponse.AdminDetailDTO detailDTO = paymentRefundService.detail(id);

        return ResponseEntity.ok().body(detailDTO);
    }

    @PostMapping("/api/refund/rejection/{id}")
    public ResponseEntity<Void> rejection(@PathVariable Long id, @RequestBody PaymentRefundRequest.RejectionDTO rejectionDTO) {

        paymentRefundService.rejection(id, rejectionDTO.getReason());

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/api/refund/approve/{id}")
    public ResponseEntity<Void> approve(@PathVariable Long id) {

        paymentRefundService.approve(id);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/admin/user/chart")
    public String getUserChart() {

        return "admin/user-chart";
    }
}
