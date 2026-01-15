package org.example.eoullimback.payment_refund;

import lombok.Data;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.timeslot.TimeSlotResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentRefundResponse {

    @Data
    public static class refundStatusDTO {
        private RefundStatus status;

        refundStatusDTO(PaymentRefund paymentRefund) {
            this.status = paymentRefund.getStatus();
        }
    }

    @Data
    public static class AdminListDTO  {
        private Long id;
        private RefundStatus refundStatus;
        private String roomName;
        private String username;
        private String paymentKey;
        private Long amount;
        private List<TimeSlotResponse.DetailDTO> timeSlots;
        private LocalDateTime createdAt;
        private String displayTime;
        private String displayStatus;
        private PaymentMethod paymentMethod;

        AdminListDTO(PaymentRefund paymentRefund, List<TimeSlotResponse.DetailDTO> timeSlots) {
            this.id = paymentRefund.getId();
            this.refundStatus = paymentRefund.getStatus();
            this.roomName = paymentRefund.getPayment().getBooking().getRoom().getName();
            this.username = paymentRefund.getPayment().getUser().getName();
            this.paymentKey = paymentRefund.getPayment().getPaymentKey();
            this.amount = paymentRefund.getPayment().getAmount();
            this.paymentMethod = paymentRefund.getPayment().getMethod();
            this.createdAt = paymentRefund.getCreatedAt();

            this.timeSlots = timeSlots;

            this.displayStatus =  switch (this.refundStatus) {
                case REQUESTED -> "환불요청";
                case COMPLETED -> "환불완료";
                case FAILED -> "환불실패";
                case REJECTED -> "환불거절";
            };

            if (!timeSlots.isEmpty()) {
                TimeSlotResponse.DetailDTO first = timeSlots.get(0);
            }
        }
    }

    @Data
    public static class AdminDetailDTO  {
        private Long id;
        private RefundStatus refundStatus;
        private String roomName;
        private String username;
        private String paymentKey;
        private Long amount;
        private List<TimeSlotResponse.DetailDTO> timeSlots;
        private LocalDateTime createdAt;
        private String displayTime;
        private String displayStatus;
        private PaymentMethod paymentMethod;
        private String reason;
        private LocalDateTime requestedAt;

        AdminDetailDTO(PaymentRefund paymentRefund, List<TimeSlotResponse.DetailDTO> timeSlots) {
            this.id = paymentRefund.getId();
            this.refundStatus = paymentRefund.getStatus();
            this.roomName = paymentRefund.getPayment().getBooking().getRoom().getName();
            this.username = paymentRefund.getPayment().getUser().getName();
            this.paymentKey = paymentRefund.getPayment().getPaymentKey();
            this.amount = paymentRefund.getPayment().getAmount();
            this.paymentMethod = paymentRefund.getPayment().getMethod();
            this.createdAt = paymentRefund.getCreatedAt();
            this.reason = paymentRefund.getReason();
            this.requestedAt = paymentRefund.getRequestedAt();

            this.timeSlots = timeSlots;

            this.displayStatus =  switch (this.refundStatus) {
                case REQUESTED -> "환불요청";
                case COMPLETED -> "환불완료";
                case FAILED -> "환불실패";
                case REJECTED -> "환불거절";
            };

            if (!timeSlots.isEmpty()) {
                TimeSlotResponse.DetailDTO first = timeSlots.get(0);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm -");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(" HH:mm까지");

//                this.displayTime = first.getStartTime().format(dateTimeFormatter)
//                        + first.getEndTime().format(timeFormatter);

                this.displayTime = first.getStartTime()
                        + first.getEndTime();
            }
        }
    }

}
