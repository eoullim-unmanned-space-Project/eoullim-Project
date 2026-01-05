package org.example.eoullimback.booking;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.User;
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
    @GetMapping("/booking/detail")
    public String detailBooking(@RequestParam("code") String bookingCode, HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new Exception401(ErrorCode.ACCESS_DENIED);
        }

        BookingResponse.DetailDTO booking = bookingService.detailBooking(sessionUser.getId(), bookingCode);

        model.addAttribute("user", sessionUser);
        model.addAttribute("booking", booking);

        return "booking/detail";
    }


    @GetMapping("/booking/complete")
    public String completeBooking(@RequestParam("code") String bookingCode, HttpSession session, Model model) {

        System.out.println(bookingCode);

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new Exception401(ErrorCode.ACCESS_DENIED);
        }

        BookingResponse.DetailDTO booking = bookingService.detailBooking(sessionUser.getId(), bookingCode);

        model.addAttribute("user", sessionUser);
        model.addAttribute("booking", booking);

        return "booking/complete";
    }

}
