package org.example.eoullimback.booking;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> calculateAmount(
            Model model,
            @RequestBody BookingRequest.CalculateAmountDTO calculateAmountDTO,
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveBooking(
            @RequestBody BookingRequest.createDTO createDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        String bookingCode = bookingService.saveBooking(user.getId(), createDTO);

        return ResponseEntity.ok().body(Map.of("bookingCode", bookingCode));
    }

    @PostMapping("/api/user/bookings/cancel/{bookingCode}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelBooking (
            @PathVariable String bookingCode,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        bookingService.cancelBooking(user.getId(), bookingCode);

        return ResponseEntity.ok().body(null);
    }
}
