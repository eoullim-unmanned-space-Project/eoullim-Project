package org.example.eoullimback.notification;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.notification.NotificationType;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.user_auth.user.MailService;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MailService mailService;

    @Override
    public void notifyPaymentSuccess(Payment payment) {

        String qrCode = generateQrCode(payment);
        String message = "결제가 완료되었습니다.";

        Notification notification = Notification.builder()
                .type(NotificationType.PAYMENT)
                .message(message)
                .qrCode(qrCode)
                .user(payment.getUser())
                .payment(payment)
                .build();


        mailService.paymentNotificationSender(payment.getUser().getEmail(), message, qrCode);
        notification.markAsSent();
        notificationRepository.save(notification);
    }

    @Override
    public void notifyPaymentFailed(Payment payment, String reason) {
        Notification notification = Notification.builder()
                .type(NotificationType.CANCEL)
                .message("결제가 실패했습니다 : " + reason)
                .user(payment.getUser())
                .payment(payment)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void notifyPaymentCancelled(Payment payment) {
        Notification notification = Notification.builder()
                .type(NotificationType.CANCEL)
                .message("결제가 취소되었습니다.")
                .user(payment.getUser())
                .payment(payment)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationEmail(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new Exception404(ErrorCode.NOTIFICATION_NOT_FOUND));

        User user = notification.getUser();

        mailService.paymentNotificationSender(user.getEmail(), notification.getMessage(), notification.getQrCode());
        notification.markAsSent();
    }

    private String generateQrCode(Payment payment) {
        return "QR-" + payment.getOrderId();
    }
}
