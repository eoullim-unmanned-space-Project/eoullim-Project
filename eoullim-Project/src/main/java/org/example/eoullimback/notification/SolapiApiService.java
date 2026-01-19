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
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolapiApiService {

    @Value("${SOLAPI_API_KEY}")
    private String apiKey;

    @Value("${SOLAPI_API_SECRET}")
    private String apiSecret;

    @Value("${SOLAPI_FROM}")
    private String from;

    // 운영 도메인으로 바꿔서 사용 (현재 값 유지)
    private final String baseUrl = "https://Eoullim.com/notifications/qr";

    private final QrCodeGenerator qrCodeGenerator;
    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);

        // 혹시 값이 비어있으면 바로 알 수 있게 로그
        log.info("[SOLAPI] init. apiKey={}, from={}", mask(apiKey), from);
    }

    public void sendPaymentSuccessSms(
            String phone,
            Payment payment,
            String qrPayload
    ) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException {

        // 1) 번호 정규화 (숫자만)
        String normFrom = normalizePhone(from);
        String normTo = normalizePhone(phone);

        if (normFrom == null || normFrom.isBlank() || normTo == null || normTo.isBlank()) {
            throw new IllegalArgumentException("SOLAPI from/to phone number is blank. from=" + from + ", to=" + phone);
        }

        // 2) QR URL
        String qrUrl = baseUrl + "?code=" + URLEncoder.encode(qrPayload, StandardCharsets.UTF_8);

        // 3) QR 이미지 생성 (대부분 PNG가 안전)
        byte[] qrImageBytes = qrCodeGenerator.generate(qrUrl);

        // 4) 임시 파일 생성 (확장자 PNG로)
        File qrFile = createTempQrFile(qrImageBytes);

        try {
            // 5) MMS 스토리지로 업로드
            String imageId = messageService.uploadFile(qrFile, StorageType.MMS);

            if (imageId == null || imageId.isBlank()) {
                throw new IllegalStateException("Solapi 이미지 업로드 실패 (imageId is null/blank)");
            }

            // 6) 문자 내용
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

            // 7) 메시지 생성
            Message message = new Message();
            message.setFrom(normFrom);
            message.setTo(normTo);
            message.setText(text);
            message.setImageId(imageId);

            // 8) 전송 + 실패 원인 로그
            try {
                messageService.send(message);
                log.info("[SOLAPI] send success. to={}, orderId={}", normTo, payment.getOrderId());
            } catch (SolapiMessageNotReceivedException e) {
                // ⭐ 핵심: 왜 실패했는지 리스트 출력
                log.error("[SOLAPI] send failed. failedMessageList={}, to={}, from={}",
                        e.getFailedMessageList(), normTo, normFrom, e);
                throw e; // 필요하면 호출부에서 잡아서 결제 흐름 안 깨지게 처리
            }

        } finally {
            if (qrFile != null && qrFile.exists()) {
                boolean deleted = qrFile.delete();
                if (!deleted) {
                    log.warn("[SOLAPI] temp qr file delete failed: {}", qrFile.getAbsolutePath());
                }
            }
        }
    }

    private File createTempQrFile(byte[] qrImageBytes) {
        try {
            // PNG로 생성 (generate가 PNG 바이트일 가능성이 커서 안전)
            File tempFile = File.createTempFile("qr_", ".png");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(qrImageBytes);
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("QR 이미지 파일 생성 실패", e);
        }
    }

    private String normalizePhone(String raw) {
        if (raw == null) return null;
        return raw.replaceAll("\\D", ""); // 숫자만 남김
    }

    private String mask(String value) {
        if (value == null) return null;
        if (value.length() <= 4) return "****";
        return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
    }
}
