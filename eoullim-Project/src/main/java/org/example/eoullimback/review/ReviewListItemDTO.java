package org.example.eoullimback.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListItemDTO {
    private Long paymentId;

    // Payment
    private String bookingCode;

    private Long roomId;
    private Long placeId;

    private String roomName;

    private Long amount;

    // 리뷰 존재 여부
    private boolean hasReview;

    // hasReview=true일 때만 값 존재
    private Long reviewId;
    private Byte rating;
    private String content;
}
