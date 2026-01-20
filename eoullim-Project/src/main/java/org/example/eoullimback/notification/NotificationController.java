package org.example.eoullimback.notification;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 내 알림 리스트
    // http://localhost:8080/notifications
    @GetMapping("/notifications")
    @PreAuthorize("hasRole('USER')")
    public String notificationList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        User user = userDetails.getUser();

        List<NotificationResponse.NotificationResponseDTO> notifications = notificationService.notificationList(user.getId());

        model.addAttribute("notifications", notifications);
        model.addAttribute("sessionUser", user);

        return "notification/list";
    }

    // 문자 메시지에서 온 URL - Qr 확인용
    // 메세지 링크 QrCode 페이지
    // http://localhost:8080/notifications/qr
    @GetMapping("/notifications/qr")
    @PreAuthorize("hasRole('USER')")
    public String showQrPage(@RequestParam("code") String code,
                             Model model,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        Notification notification = notificationService.validateQr(code);

        model.addAttribute("qrPayload", notification.getQrCode());
        model.addAttribute("createdAt", notification.getCreatedAt());

        return "notification/qr";
    }
}
