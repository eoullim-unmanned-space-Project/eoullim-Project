package org.example.eoullimback.booking;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * 화면 - 예약 화면
     */
    @GetMapping("/user/bookings/detail")
    @PreAuthorize("hasRole('USER')")
    public String detailBooking(
            @RequestParam("code") String bookingCode,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {

        User user = userDetails.getUser();

        BookingResponse.DetailDTO booking = bookingService.detailBooking(user.getId(), bookingCode);

        model.addAttribute("user", user);
        model.addAttribute("booking", booking);

        return "booking/detail";
    }

    @GetMapping("/user/bookings/complete")
    @PreAuthorize("hasRole('USER')")
    public String completeBooking(
            @RequestParam("code") String bookingCode,
            @AuthenticationPrincipal CustomUserDetails userDetails ,
            Model model
    ) {

        User user = userDetails.getUser();

        BookingResponse.DetailDTO booking = bookingService.detailBooking(user.getId(), bookingCode);

        model.addAttribute("user", user);
        model.addAttribute("booking", booking);

        return "booking/complete";
    }

}
