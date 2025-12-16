package org.example.eoullimback.payment_refund;

import jakarta.persistence.*;
import lombok.*;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback.payment.Payment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_refunds",
    indexes = {
        @Index(name = "idx_payment_refunds_payment_id", columnList = "payment_id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentRefund extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false)
    private Long amount;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RefundStatus status;

    @Column(length = 50)
    private String failureCode;

    @Column(length = 255)
    private String failureMessage;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;

    @Builder
    public PaymentRefund(Payment payment, Long amount, String reason, RefundStatus status, LocalDateTime requestedAt) {
        this.payment = payment;
        this.amount = amount;
        this.reason = reason;
        this.status = (status != null) ? status : RefundStatus.REQUESTED;
        this.requestedAt = (requestedAt != null) ? requestedAt : LocalDateTime.now();
    }

    public void markCompleted() {
        this.status = RefundStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.failureCode = null;
        this.failureMessage = null;
    }

    public void markFailed(String failureCode, String failureMessage) {
        this.status = RefundStatus.FAILED;
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
    }

}
