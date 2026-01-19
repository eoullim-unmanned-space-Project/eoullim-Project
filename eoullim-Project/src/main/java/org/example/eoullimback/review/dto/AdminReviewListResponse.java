package org.example.eoullimback.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminReviewListResponse {
    private Long reviewId;
    private Byte rating;
    private String content;

    private Long userId;
    private String userName;

    private Long roomId;
    private String roomName;
    private Long placeId;

    private Long paymentId;
    private String bookingCode;
    private Long amount;

    private java.time.LocalDateTime createdAt;
}
