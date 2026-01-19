package org.example.eoullimback.notification;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // http://localhost:8080/notifications
    @GetMapping("/notifications")
    public String notificationList(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        List<NotificationResponse.NotificationResponseDTO> notifications = notificationService.notificationList(sessionUser.getId());

        model.addAttribute("notifications", notifications);
        model.addAttribute("sessionUser", sessionUser);

        return "notification/list";
    }

    // 메세지 링크 QrCode 페이지
    // http://localhost:8080/notifications/qr
    @GetMapping("/notifications/qr")
    public String showQrPage(@RequestParam("code") String code,
                             Model model) {

        Notification notification = notificationService.validateQr(code);

        model.addAttribute("qrPayload", notification.getQrCode());
        model.addAttribute("createdAt", notification.getCreatedAt());

        return "notification/qr";
    }
}
