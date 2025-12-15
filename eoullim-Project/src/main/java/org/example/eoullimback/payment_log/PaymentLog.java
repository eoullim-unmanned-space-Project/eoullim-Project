package org.example.eoullimback.payment_log;

import jakarta.persistence.*;
import lombok.*;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.payment.ActionType;
import org.example.eoullimback._common.enums.payment.PaymentLogStatus;

@Entity
@Table(name = "payment_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "payment_refund_id")
    private Long paymentRefundId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentLogStatus status;

    @Builder
    public PaymentLog(Long paymentId, Long paymentRefundId, Long amount, ActionType actionType, PaymentLogStatus status) {
        this.paymentId = paymentId;
        this.paymentRefundId = paymentRefundId;
        this.amount = amount;
        this.status = (status != null) ? status : PaymentLogStatus.PENDING;
        this.actionType = (actionType != null) ? actionType : ActionType.PAYMENT;
    }

    public static PaymentLog createPayment(Long paymentId, Long amount) {
        return PaymentLog.builder()
                .paymentId(paymentId)
                .amount(amount)
                .build();
    }

    public static PaymentLog createRefund(Long paymentId, Long paymentRefundId, Long amount) {
        return PaymentLog.builder()
                .paymentId(paymentId)
                .paymentRefundId(paymentRefundId)
                .amount(amount)
                .actionType(ActionType.REFUND)
                .build();
    }

    public void markSuccess() {
        this.status = PaymentLogStatus.COMPLETED;
    }

    public void markFailed() {
        this.status = PaymentLogStatus.FAILED;
    }
}
