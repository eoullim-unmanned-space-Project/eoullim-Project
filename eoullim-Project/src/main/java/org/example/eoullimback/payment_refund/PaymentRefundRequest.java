package org.example.eoullimback.payment_refund;

import lombok.Data;

public class PaymentRefundRequest {

    @Data
    public static class CreateRefundDTO {
        private String paymentKey;
        private String reason;
    }
}
