package org.example.eoullimback.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class PaymentResponse {

    @Data
    public static class PrepareDTO{
        private String paymentId;
        private Long totalAmount;

        public PrepareDTO(String paymentId, Long totalAmount) {
            this.paymentId = paymentId;
            this.totalAmount = totalAmount;
        }
    }

    @Data
    public static class PortOneDTO {
        private int code;
        private String message;
        private TokenData response;
    }

    @Data
    public static class TokenData {
        @JsonProperty("access_token")
        private String accessToken;
    }

    @Data
    public static class PortOnePaymentDetailDTO {
        private int code;
        private String message;
        private PaymentData response;
    }

    @Data
    static class PaymentData {
        private String status;
        private Long amount;

        @JsonProperty("merchant_uid")
        private String merchantUid;

        @JsonProperty("fail_reason")
        private String failReason;
    }
}
