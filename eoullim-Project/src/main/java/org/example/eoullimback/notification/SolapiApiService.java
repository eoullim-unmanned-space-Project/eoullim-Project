package org.example.eoullimback.notification;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolapiApiService {

    @Value("${SOLAPI_API_KEY}")
    String apiKey;

    @Value("${SOLAPI_API_SECRET}")
    String apiSecret;

    @Value("${SOLAPI_FROM}")
    String from;

    String baseUrl = "http://localhost:8080/notifications/qr";

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService =
                SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
    }

    public void sendPaymentSuccessSms(
            String phone,
            Payment payment,
            String qrPayload
    ) throws SolapiEmptyResponseException,
            SolapiUnknownException,
            SolapiMessageNotReceivedException {

        String qrUrl = baseUrl + "?code=" + qrPayload;

        String text = """
                [Eoullim 결제 완료]

                상품명: %s
                결제 금액: %,d원
                주문 번호: %s

                QR 코드 확인:
                %s

                ※ 본 QR 코드는 본인만 사용 가능하며 타인에게 공유할 수 없습니다.
                """.formatted(
                payment.getProductName(),
                payment.getAmount(),
                payment.getOrderId(),
                qrUrl
        );

        Message message = new Message();
        message.setFrom(from); // 등록된 발신번호
        message.setTo(phone);
        message.setText(text);

        messageService.send(message);
    }
}
