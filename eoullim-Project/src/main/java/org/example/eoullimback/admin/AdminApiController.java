package org.example.eoullimback.admin;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundRequest;
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
import org.example.eoullimback.user_auth.user.dto.response.UserCountResult;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.example.eoullimback._common.util.NumberFormatUtils.*;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final PaymentRefundService paymentRefundService;
    private final PlaceService placeService;
    private final RoomService roomService;
    private final UserService userService;
    private final DashboardUserService dashboardUserService;

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

    @GetMapping("/api/admin/category")
    public ResponseEntity<List<PaymentRefundResponse.RefundCategoryCountDTO>> getCategory() {

        List<PaymentRefundResponse.RefundCategoryCountDTO> categoryCountList = paymentRefundService.getRefundCategoryCounts();

        return ResponseEntity.ok().body(categoryCountList);
    }

    @GetMapping("/admin/user/chart")
    public String getUserChart() {

        return "admin/user-chart";
    }

    // =====
    // 장소(place)
    // ====

    @PostMapping("/api/admin/places")
    public ResponseEntity<Place> createProc(PlaceRequest.CreateDTO request) {

        request.validate();
        Place place = placeService.placeCreate(request);

        return ResponseEntity.ok().body(place);
    }

    @PutMapping("/api/admin/places/{placeId}")
    public ResponseEntity<Map<String, Place>> UpdateProc(@PathVariable Long placeId,
                                                         PlaceRequest.UpdateDTO request
    ) {
        request.validate();

        Place place = placeService.placeUpdate(placeId, request);

        return ResponseEntity.ok().body(Map.of("place", place));
    }

    @DeleteMapping("/api/admin/places/{id}")
    public ResponseEntity<Void> deleteProc(@PathVariable(name = "id") Long placeId) {

        placeService.placeDelete(placeId);

        return ResponseEntity.ok().body(null);

    }

    // =====
    // 방(room)
    // ====

    @PostMapping("/api/admin/rooms")
    public ResponseEntity<Room> createRoom(Model model,
                                           RoomRequest.CreateDTO createDTO
    ) {
        createDTO.validate();

        Room room = roomService.createRoom(createDTO);
        model.addAttribute("room", room);

        return ResponseEntity.ok().body(room);
    }

    @PutMapping("/api/admin/rooms/{roomId}")
    public ResponseEntity<Map<String, Room>> updateRoom(@PathVariable Long roomId,
                                                        RoomRequest.UpdateDTO updateDTO
    ) {

        updateDTO.validate();

        Room room = roomService.updateRoom(roomId, updateDTO);

        return ResponseEntity.ok().body(Map.of("room", room));
    }


    @DeleteMapping("/api/admin/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {

        roomService.deleteRoom(roomId);

        return ResponseEntity.ok().body(null);
    }
}
