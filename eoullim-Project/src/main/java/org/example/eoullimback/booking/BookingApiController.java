package org.example.eoullimback.booking;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookingApiController {

    private final BookingService bookingService;

    /**
     * 기능: amount 요금계산
     */
    @PostMapping("/api/calculate-amount")
    public ResponseEntity<?> calculateAmount(Model model, @RequestBody BookingRequest.CalculateAmountDTO calculateAmountDTO, HttpSession session) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("message", "로그인이 필요합니다"));
        }

        BookingResponse.CalculateAmountDTO amount = bookingService.calculateAmount(calculateAmountDTO);

        return ResponseEntity.ok().body(Map.of("amount", amount));
    }
}
