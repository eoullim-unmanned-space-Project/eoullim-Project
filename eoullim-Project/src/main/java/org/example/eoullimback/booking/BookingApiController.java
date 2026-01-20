package org.example.eoullimback.booking;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookingApiController {

    private final BookingService bookingService;

    /**
     * 기능: amount 요금계산
     */
    @PostMapping("/api/user/bookings/amount")
    public ResponseEntity<?> calculateAmount(Model model, @RequestBody BookingRequest.CalculateAmountDTO calculateAmountDTO,
                                             @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        BookingResponse.CalculateAmountDTO amount = bookingService.calculateAmount(calculateAmountDTO);

        return ResponseEntity.ok().body(Map.of("amount", amount));
    }

    /**
     * 기능 - 부킹 생성
     */
    @PostMapping("/api/user/bookings")
    public ResponseEntity<?> saveBooking(
            @RequestBody BookingRequest.createDTO createDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        String bookingCode = bookingService.saveBooking(user.getId(), createDTO);

        return ResponseEntity.ok().body(Map.of("bookingCode", bookingCode));
    }

    @PostMapping("/api/user/bookings/cancel/{bookingCode}")
    public ResponseEntity<?> cancelBooking (
            @PathVariable String bookingCode,
            HttpSession session
    ) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        bookingService.cancelBooking(sessionUser.getId(), bookingCode);

        return ResponseEntity.ok().body(null);
    }
}
