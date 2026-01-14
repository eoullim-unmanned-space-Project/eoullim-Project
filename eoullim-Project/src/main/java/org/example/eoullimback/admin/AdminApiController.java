package org.example.eoullimback.admin;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceRequest;
import org.example.eoullimback.place.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final PaymentRefundService paymentRefundService;
    private final PlaceService placeService;

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
    // =====
    // 장소
    // ====

    @PostMapping("/place/create")
    public ResponseEntity<?> createProc(PlaceRequest.CreateDTO request) {

        request.validate();
        Place place = placeService.placeCreate(request);

        return ResponseEntity.ok().body(place);
    }

    @PutMapping("/place/{placeId}")
    public ResponseEntity<Map<String, Place>> UpdateProc(@PathVariable Long placeId,
                             PlaceRequest.UpdateDTO request
    ) {
        request.validate();

        Place place = placeService.placeUpdate(placeId, request);

        return ResponseEntity.ok().body(Map.of("place", place));
    }

    @DeleteMapping("/place/{id}")
    public ResponseEntity<Void> deleteProc(@PathVariable(name = "id") Long placeId) {

        placeService.placeDelete(placeId);

        return ResponseEntity.ok().body(null);

    }
}
