package org.example.eoullimback.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.eoullimback._common.enums.payment.PaymentStatus;

@Getter
@AllArgsConstructor
public class ReviewListItemDTO {
    private Long paymentId;

    private String bookingCode;

    private Long roomId;
    private Long placeId;

    private String roomName;

    private Long amount;

    private PaymentStatus paymentStatus;

    private boolean hasReview;

    private Long reviewId;
    private Byte rating;
    private String content;
}
