package org.example.eoullimback.admin;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceRequest;
import org.example.eoullimback.place.PlaceService;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRequest;
import org.example.eoullimback.room.RoomService;
import org.example.eoullimback.user_auth.user.DashboardUserService;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final PaymentRefundService paymentRefundService;
    private final PlaceService placeService;
    private final RoomService roomService;
    private final UserService userService;
    private final DashboardUserService dashboardUserService;

    @GetMapping("/api/admin/category")
    public ResponseEntity<List<PaymentRefundResponse.RefundCategoryCountDTO>> getCategory() {

        List<PaymentRefundResponse.RefundCategoryCountDTO> categoryCountList = paymentRefundService.getRefundCategoryCounts();

        System.out.println("==================================================");
        System.out.println(categoryCountList);
        return ResponseEntity.ok().body(categoryCountList);
    }


}
