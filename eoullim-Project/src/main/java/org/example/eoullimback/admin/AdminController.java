package org.example.eoullimback.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.place.Place;
import org.example.eoullimback.place.PlaceResponse;
import org.example.eoullimback.place.PlaceService;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final PaymentRefundService paymentRefundService;
    private final PlaceService placeService;

    // http://localhost:8080/admin/dashboard
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        model.addAttribute("user", sessionUser);
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
        model.addAttribute("placeKeyword", keyword != null ? keyword : "");

        return "/admin/place";
    }
}
