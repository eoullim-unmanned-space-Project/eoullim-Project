package org.example.eoullimback.payment;

import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;

public interface PaymentService {
    PaymentResponse.PrepareDTO prepare(Long id, String bookingCode);
    String complete(Long id, String impUid, String merchantUid) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException;
    void cancel(String paymentKey);
}
