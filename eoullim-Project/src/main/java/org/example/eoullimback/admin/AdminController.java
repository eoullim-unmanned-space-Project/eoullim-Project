package org.example.eoullimback.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final PaymentRefundService paymentRefundService;

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

        List<PaymentRefundResponse.AdminListDTO> refundList  = paymentRefundService.getRefundList();

        model.addAttribute("refundList", refundList);

        return "admin/refund";
    }
}
