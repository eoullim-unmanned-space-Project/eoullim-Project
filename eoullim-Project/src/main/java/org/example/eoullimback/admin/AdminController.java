package org.example.eoullimback.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback._common.enums.place.Category;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceResponse;
import org.example.eoullimback.place.PlaceService;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomResponse;
import org.example.eoullimback.room.RoomService;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.DashboardUserService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.dto.response.UserCountResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.example.eoullimback._common.util.NumberFormatUtils.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final PaymentRefundService paymentRefundService;
    private final PlaceService placeService;
    private final RoomService roomService;
    private final DashboardUserService dashboardUserService;

    // http://localhost:8080/admin/dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        UserCountResult result = dashboardUserService.getUserCount();

        model.addAttribute("user", sessionUser);

        // User 통계
        long today = result.getTodayCount();
        long yesterday = result.getYesterdayCount();

        model.addAttribute("totalUserComma", formatComma(today));
        model.addAttribute("totalUserK", formatK(today));
        model.addAttribute("increaseRate", calculateRate(today, yesterday));
        model.addAttribute("isIncrease", today >= yesterday);

        return "admin/dashboard";
    }

    @GetMapping("/refund")
    public String payment(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        List<PaymentRefundResponse.AdminListDTO> refundList = paymentRefundService.getRefundList();

        model.addAttribute("refundList", refundList);

        return "admin/refund";
    }

    @GetMapping("/place")
    @PreAuthorize("hasRole('ADMIN')")
    public String place(Model model,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(required = false) String placeKeyword,
                        @RequestParam(required = false) String roomKeyword,
                        @RequestParam(required = false) Category category,
                        @RequestParam(required = false) RoomStatus status
    ) {
        User user = userDetails.getUser();

        int pageIndex = Math.max(0, page - 1);
        PageResponse.PageDTO<Place, PlaceResponse.DetailDTO> placePage = placeService.placeAdminList(pageIndex, size, placeKeyword, category);
        model.addAttribute("placePage", placePage);
        PageResponse.PageDTO<Room, RoomResponse.AdminDetailDTO> roomList = roomService.roomAdminList(pageIndex, size, roomKeyword, status);

        model.addAttribute("placeKeyword", placeKeyword != null ? placeKeyword : "");
        model.addAttribute("roomKeyword", roomKeyword != null ? roomKeyword : "");
        model.addAttribute("roomList", roomList);
        model.addAttribute("category", category);

        return "/admin/place";
    }
}
