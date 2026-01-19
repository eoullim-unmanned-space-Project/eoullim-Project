package org.example.eoullimback.admin;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final PaymentRefundService paymentRefundService;

    @GetMapping("api/admin/refund/detail/{id}")
    public ResponseEntity<PaymentRefundResponse.AdminDetailDTO> detail(@PathVariable Long id) {

        PaymentRefundResponse.AdminDetailDTO detailDTO = paymentRefundService.detail(id);

        return ResponseEntity.ok().body(detailDTO);
    }

    @PostMapping("/api/admin/refund/rejection/{id}")
    public ResponseEntity<Void> rejection(@PathVariable Long id, @RequestBody PaymentRefundRequest.RejectionDTO rejectionDTO) {

        paymentRefundService.rejection(id, rejectionDTO.getReason());

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/api/admin/refund/approve/{id}")
    public ResponseEntity<Void> approve(@PathVariable Long id) {

        paymentRefundService.approve(id);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/api/admin/category")
    public ResponseEntity<List<PaymentRefundResponse.RefundCategoryCountDTO>> getCategory() {

        List<PaymentRefundResponse.RefundCategoryCountDTO> categoryCountList = paymentRefundService.getRefundCategoryCounts();

        return ResponseEntity.ok().body(categoryCountList);
    }
}
