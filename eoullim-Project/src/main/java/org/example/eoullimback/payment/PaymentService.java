package org.example.eoullimback.payment;

import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;

public interface PaymentService {
    PaymentResponse.PrepareDTO prepare(Long id, String bookingCode);
    String complete(Long id, String impUid, String merchantUid) throws SolapiEmptyResponseException, SolapiUnknownException, SolapiMessageNotReceivedException;
    void cancel(String paymentKey);
    UserResponse.UserPaymentDTO paymentDetail(String bookingCode, Long id);
}
