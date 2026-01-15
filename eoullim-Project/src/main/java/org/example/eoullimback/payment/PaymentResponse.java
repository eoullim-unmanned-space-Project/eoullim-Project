package org.example.eoullimback.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback.payment_refund.PaymentRefund;
import org.example.eoullimback.timeslot.TimeSlotResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentResponse {

    @Data
    public static class PrepareDTO{
        private String paymentKey;
        private Long totalAmount;

        public PrepareDTO(String paymentKey, Long totalAmount) {
            this.paymentKey = paymentKey;
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

    @Data
    public static class PaymentListDTO {
        private String paymentKey;
        private String name;
        private String placeName;
        private String roomName;
        private Long amount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private LocalDateTime approvedAt;
        private String displayStatus;

        public PaymentListDTO(Payment payment) {
            this.name = payment.getUser().getName();
            this.placeName = payment.getBooking().getRoom().getPlace().getName();
            this.roomName = payment.getBooking().getRoom().getName();
            this.amount = payment.getAmount();
            this.paymentMethod = payment.getMethod();
            this.paymentStatus = payment.getStatus();
            this.approvedAt = payment.getApprovedAt();
            this.paymentKey = payment.getPaymentKey();

            this.displayStatus =  switch (this.paymentStatus) {
                case READY -> "결제준비";
                case REFUNDED -> "환불완료";
                case CANCELLED -> "결제취소";
                case FAILED -> "결제실패";
                case SUCCESS -> "결제성공";
                case COMPLETED -> "이용완료";
            };
        }
    }

    @Data
    public static class PaymentDetailDTO {
        private String name;
        private String roomName;
        private int qty;
        private Long amount;
        private List<TimeSlotResponse.DetailDTO> timeSlots;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private LocalDateTime approvedAt;
        private String displayTime;
        private String bookingCode;
        private String displayStatus;
        private String paymentKey;
        private RefundStatus refundStatus;

        public PaymentDetailDTO(Payment payment, List<TimeSlotResponse.DetailDTO> timeSlots, PaymentRefund paymentRefund) {
            this.name = payment.getUser().getName();
            this.roomName = payment.getBooking().getRoom().getName();
            this.amount = payment.getAmount();
            this.qty = payment.getBooking().getQty();
            this.paymentMethod = payment.getMethod();
            this.paymentStatus = payment.getStatus();
            this.approvedAt = payment.getApprovedAt();
            this.timeSlots = timeSlots;
            this.bookingCode = payment.getOrderId();
            this.paymentKey = payment.getPaymentKey();
            this.refundStatus = (paymentRefund != null) ? paymentRefund.getStatus() : null;

            this.displayStatus =  switch (this.paymentStatus) {
                case READY -> "결제준비";
                case REFUNDED -> "환불완료";
                case CANCELLED -> "결제취소";
                case FAILED -> "결제실패";
                case SUCCESS -> "결제성공";
                case COMPLETED -> "이용완료";
            };

            if (!timeSlots.isEmpty()) {
                TimeSlotResponse.DetailDTO first = timeSlots.get(0);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm -");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(" HH:mm까지");

                this.displayTime = first.getStartTime().format(dateTimeFormatter)
                        + first.getEndTime().format(timeFormatter);
            }
        }
    }

    @Data
    public static class SalesResponseDto {
        private Long todaySales;
        private Long yesterdaySales;

        public SalesResponseDto(Long todaySales, Long yesterdaySales) {
            this.todaySales = todaySales;
            this.yesterdaySales = yesterdaySales;
        }
    }

}
