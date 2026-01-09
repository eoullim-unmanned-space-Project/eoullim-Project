package org.example.eoullimback.notification;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.model.StorageType;
import com.solapi.sdk.message.service.DefaultMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SolapiApiService {

    @Value("${SOLAPI_API_KEY}")
    String apiKey;

    @Value("${SOLAPI_API_SECRET}")
    String apiSecret;

    @Value("${SOLAPI_FROM}")
    String from;

    String baseUrl = "https://Eoullim.com/notifications/qr";

    private final QrCodeGenerator qrCodeGenerator;
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

        String qrUrl = baseUrl + "?code=" + URLEncoder.encode(qrPayload, StandardCharsets.UTF_8);;

        byte[] qrImageBytes = qrCodeGenerator.generate(qrUrl);

        File qrFile = createTempQrFile(qrImageBytes);

        try {
            String imageId = messageService.uploadFile(
                    qrFile,
                    StorageType.MMS
            );

            if (imageId == null) {
                throw new IllegalStateException("Solapi 이미지 업로드 실패");
            }

            String text = """
                    [Eoullim 결제 완료]

                    상품명: %s
                    결제 금액: %,d원
                    주문 번호: %s

                    아래 QR 코드를 스캔하거나
                    링크를 눌러 확인하세요.

                    %s

                    ※ 본 QR 코드는 본인만 사용 가능하며
                    타인에게 공유할 수 없습니다.
                    """.formatted(
                    payment.getProductName(),
                    payment.getAmount(),
                    payment.getOrderId(),
                    qrUrl
            );

            Message message = new Message();
            message.setFrom(from);
            message.setTo(phone);
            message.setText(text);
            message.setImageId(imageId);

            messageService.send(message);

        } finally {
            qrFile.delete();
        }
    }

    private File createTempQrFile(byte[] qrImageBytes) {
        try {
            File tempFile = File.createTempFile("qr_", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(qrImageBytes);
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("QR 이미지 파일 생성 실패", e);
        }
    }
}
