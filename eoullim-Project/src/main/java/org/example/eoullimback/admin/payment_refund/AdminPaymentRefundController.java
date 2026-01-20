package org.example.eoullimback.admin.payment_refund;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment_refund.PaymentRefundResponse;
import org.example.eoullimback.payment_refund.PaymentRefundService;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminPaymentRefundController {

    private final PaymentRefundService paymentRefundService;

    @GetMapping("/admin/refunds/{id}/detail")
    public ResponseEntity<PaymentRefundResponse.AdminDetailDTO> detail(@PathVariable Long id) {

        PaymentRefundResponse.AdminDetailDTO detailDTO = paymentRefundService.detail(id);

        return ResponseEntity.ok().body(detailDTO);
    }

    @GetMapping("/admin/refund")
    public String payment(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        User user = userDetails.getUser();

        List<PaymentRefundResponse.AdminListDTO> refundList = paymentRefundService.getRefundList();

        model.addAttribute("refundList", refundList);

        return "admin/refund";
    }

}
