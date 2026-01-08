package org.example.eoullimback.notification;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolapiApiService {

    @Value("${SOLAPI_API_KEY}")
    String apiKey;

    @Value("${SOLAPI_API_SECRET}")
    String apiSecret;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService =
                SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
    }

    public void sendQrUrl(String phone, String qrPayload) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException {
        Message message = new Message();
        message.setFrom("01031153952"); // 등록된 발신번호
        message.setTo(phone);
        message.setText(
                "결제가 완료되었습니다.\nQR 확인: " +
                        "https://our-site.com/qr?code=" + qrPayload
        );

        messageService.send(message);
    }
}
