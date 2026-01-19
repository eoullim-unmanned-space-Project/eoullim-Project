package org.example.eoullimback.notification;

import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.notification.NotificationType;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.payment.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SolapiApiService solapiApiService;

    @Override
    public List<NotificationResponse.NotificationResponseDTO> notificationList(Long id) {

        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(id);

        return notifications.stream()
                .map(NotificationResponse.NotificationResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public void notifyPaymentSuccess(Payment payment) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException {

        String qrPayload = "ENTRY_" + payment.getOrderId();

        Notification notification = Notification.builder()
                .type(NotificationType.PAYMENT)
                .message("결제가 완료되었습니다.")
                .qrCode(qrPayload)
                .user(payment.getUser())
                .payment(payment)
                .build();

        notificationRepository.save(notification);

        solapiApiService.sendPaymentSuccessSms(
                payment.getUser().getPhone(),
                payment,
                qrPayload
        );

        notification.markAsSent();
    }

    @Override
    @Transactional
    public void notifyPaymentFailed(Payment payment, String reason) {
        Notification notification = Notification.builder()
                .type(NotificationType.CANCEL)
                .message("결제가 실패했습니다 : " + reason)
                .user(payment.getUser())
                .payment(payment)
                .build();

        notification.markAsFail();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void notifyPaymentCancelled(Payment payment) {
        Notification notification = Notification.builder()
                .type(NotificationType.CANCEL)
                .message("결제가 취소되었습니다.")
                .user(payment.getUser())
                .payment(payment)
                .build();

        notification.markAsCancel();
    }

    @Override
    public Notification validateQr(String code) {
        return notificationRepository.findByQrCode(code)
                .orElseThrow(() -> new Exception400(ErrorCode.INVALID_QR_CODE));
    }

    @Override
    @Transactional
    public void useQrcode(Long userId, Long id) {

        Notification notification = notificationRepository.findByIdWithUser(userId, id).orElseThrow(()-> new Exception404(ErrorCode.NOTIFICATION_NOT_FOUND));

        System.out.println(notification);

        Payment payment = notification.getPayment();

        notification.markAsComplete();

        payment.markCompleted();

        log.info("사용자가 QrCode를 사용했습니다. Payment의 상태값은 이용완료로 바뀝니다. Payment 상태: {}", payment.getStatus());
    }

}
