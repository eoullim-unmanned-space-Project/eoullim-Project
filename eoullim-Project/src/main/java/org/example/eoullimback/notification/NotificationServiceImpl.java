package org.example.eoullimback.notification;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.notification.NotificationType;
import org.example.eoullimback.payment.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void notifyPaymentSuccess(Payment payment) {
        Notification notification = Notification.builder()
                .type(NotificationType.PAYMENT)
                .message("결제가 완료되었습니다.")
                .qrCode(generateQrCode(payment))
                .user(payment.getUser())
                .payment(payment)
                .build();

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

    private String generateQrCode(Payment payment) {
        return "QR-" + payment.getOrderId();
    }
}
