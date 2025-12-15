package org.example.eoullimback.payment_log;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.payment.ActionType;
import org.example.eoullimback._common.enums.payment.PaymentLogStatus;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment_refund.PaymentRefund;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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

    public void markSuccess() {
        this.status = PaymentLogStatus.COMPLETED;
    }

    public void markFailed() {
        this.status = PaymentLogStatus.FAILED;
    }
}
