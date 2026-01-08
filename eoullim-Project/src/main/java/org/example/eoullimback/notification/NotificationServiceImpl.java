package org.example.eoullimback.notification;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.notification.NotificationType;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.user_auth.user.MailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MailService mailService;
    private final QrCodeGenerator qrCodeGenerator;

    @Override
    public List<NotificationResponse.NotificationResponseDTO> notificationList(Long id) {

        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(id);

        return notifications.stream()
                .map(NotificationResponse.NotificationResponseDTO::new)
                .toList();
    }

    @Override
    public void notifyPaymentSuccess(Payment payment) {

        String qrPayload = "ENTRY|" + payment.getOrderId();
        byte[] qrImage = qrCodeGenerator.generate(qrPayload);

        Notification notification = Notification.builder()
                .type(NotificationType.PAYMENT)
                .message("결제가 완료되었습니다.")
                .qrCode(qrPayload)
                .user(payment.getUser())
                .payment(payment)
                .build();

        notificationRepository.save(notification);

        mailService.sendPaymentSuccessMail(
                payment.getUser().getEmail(),
                payment,
                qrImage
        );

        notification.markAsSent();
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

}
