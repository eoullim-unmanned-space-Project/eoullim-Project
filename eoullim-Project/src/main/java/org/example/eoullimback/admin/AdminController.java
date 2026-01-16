package org.example.eoullimback.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceResponse;
import org.example.eoullimback.place.PlaceService;
import org.example.eoullimback.room.RoomResponse;
import org.example.eoullimback.room.RoomService;
import org.example.eoullimback.user_auth.user.DashboardUserService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.example.eoullimback.user_auth.user.dto.response.UserCountResult;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.example.eoullimback._common.util.NumberFormatUtils.*;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final PaymentRefundService paymentRefundService;
    private final PlaceService placeService;
    private final UserService userService;
    private final RoomService roomService;
    private final DashboardUserService dashboardUserService;

    // http://localhost:8080/admin/dashboard
    @GetMapping("/admin/dashboard")
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

    @GetMapping("/admin/refund")
    public String payment(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        List<PaymentRefundResponse.AdminListDTO> refundList = paymentRefundService.getRefundList();

        model.addAttribute("refundList", refundList);

        return "admin/refund";
    }

    @GetMapping("/admin/place")
    public String place(Model model,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);
        PageResponse.PageDTO<Place, PlaceResponse.DetailDTO> placePage = placeService.placeAdminList(pageIndex, size, keyword);
        model.addAttribute("placePage", placePage);
        List<RoomResponse.AdminDetailDTO> roomList = roomService.roomAdminList();

        model.addAttribute("placeKeyword", keyword != null ? keyword : "");
        model.addAttribute("roomList", roomList);

        return "/admin/place";
    }

    @GetMapping("/admin/users")
    public String users(HttpSession session,Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        List<UserResponse.AdminListDTO> userList = userService.getUserList();
        model.addAttribute("adminUserList", userList);

        return "admin/user";
    }

    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<UserResponse.AdminUserDetailDTO> getUserDetail(@PathVariable Long userId) {
        User user = userService.findById(userId);

        return ResponseEntity.ok(new UserResponse.AdminUserDetailDTO(user));
    }

}
