package org.example.eoullimback.notification;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception404;
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
    private final UserRepository userRepository;

    // http://localhost:8080/notifications
    @GetMapping("/notifications")
    public String notificationList(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");
//        User sessionUser = getLoginUserId(session);

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

//     TODO : 유저 더미 (삭제 예정)
    private User getLoginUserId(HttpSession session) {

        Long userId = (Long) session.getAttribute("sessionUser");

        if (userId == null) {
            userId = 1L;
            session.setAttribute("sessionUser", userId);
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));
    }




}
