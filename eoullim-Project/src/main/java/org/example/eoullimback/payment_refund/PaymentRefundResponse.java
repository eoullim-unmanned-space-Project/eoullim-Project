package org.example.eoullimback.payment_refund;

import lombok.Data;
import org.example.eoullimback._common.enums.payment.RefundStatus;

public class PaymentRefundResponse {

    @Data
    public static class refundStatusDTO {
        private RefundStatus status;

        refundStatusDTO(PaymentRefund paymentRefund) {
            this.status = paymentRefund.getStatus();
        }
    }


}
