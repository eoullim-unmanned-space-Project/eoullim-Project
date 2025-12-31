package org.example.eoullimback.booking;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * 기능 - 부킹 생성
     */
    @PostMapping("/room/{roomId}/timeSlot/{timeSlotId}/bookings")
    public String saveBooking(@PathVariable Long roomId,
                              @PathVariable Long timeSlotId,
                              HttpSession session,
                              @ModelAttribute @Valid BookingRequest.createDTO createDTO
    ) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        Booking booking = bookingService.saveBooking(sessionUser.getId(), roomId, timeSlotId, createDTO);

        return "/bookig/bookings" + booking.getId();
    }

//    /**
//     * 화면 - 예약 화면
//     */
//    @GetMapping()
//    public String detailBooking() {
//
//        return "/booking/";
//    }

}
