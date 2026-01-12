package org.example.eoullimback.notification;

import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import org.example.eoullimback.payment.Payment;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse.NotificationResponseDTO> notificationList(Long id);
    void notifyPaymentSuccess(Payment payment) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException;
    void notifyPaymentFailed(Payment payment, String reason);
    void notifyPaymentCancelled(Payment payment);
    Notification validateQr(String code);

     void useQrcode(Long userId, Long id);
}
