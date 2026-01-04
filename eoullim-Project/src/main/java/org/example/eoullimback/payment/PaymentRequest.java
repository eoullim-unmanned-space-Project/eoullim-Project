package org.example.eoullimback.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class PaymentRequest {

    @Data
    public static class PrepareDTO {
        String bookingCode;
    }

    @Data
    public static class CompleteDTO {
        @JsonProperty("imp_uid")
        String impUid;

        @JsonProperty("merchant_uid")
        String merchantUid;
    }

}
