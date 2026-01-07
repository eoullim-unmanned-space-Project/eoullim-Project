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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // http://localhost:8080/notifications
    @GetMapping("/notifications")
    public String notificationList(HttpSession session, Model model) {

//        Long sessionUserId = (Long) session.getAttribute("sessionUserId");
//        if (sessionUserId == null) {
//            throw new Exception404(ErrorCode.USER_NOT_FOUND);
//        }
//
//        User sessionUser = userRepository.findById(sessionUserId).orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));
        User sessionUser = getLoginUserId(session);

        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(sessionUser.getId());

        List<NotificationResponse.NotificationResponseDTO> res = notifications.stream()
                .map(NotificationResponse.NotificationResponseDTO::new)
                .toList();

        model.addAttribute("notifications", res);
        model.addAttribute("sessionUser", sessionUser);

        return "notification/list";
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
