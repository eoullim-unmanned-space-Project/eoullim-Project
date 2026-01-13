package org.example.eoullimback.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserService;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final PaymentRefundService paymentRefundService;
    private final UserService userService;

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
