package org.example.eoullimback.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ReviewablePaymentDTO {
    private Long paymentId;
    private Long roomId;
    private Long placeId;

    public ReviewablePaymentDTO(Long paymentId, Long placeId, Long roomId) {
        this.paymentId = paymentId;
        this.roomId = roomId;
        this.placeId = placeId;
    }

    public Long getPaymentId() { return paymentId; }
    public Long getRoomId() { return roomId; }
    public Long getPlaceId() { return placeId; }
}
