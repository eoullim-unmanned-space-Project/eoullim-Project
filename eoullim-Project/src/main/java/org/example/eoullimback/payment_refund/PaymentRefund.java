package org.example.eoullimback.payment_refund;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.payment.RefundStatus;
import org.example.eoullimback.payment.Payment;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_refunds",
    indexes = {
        @Index(name = "idx_payment_refunds_payment_id", columnList = "payment_id")
    }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RefundStatus status;

    @Column(length = 50)
    private String failureCode;

    @Column(length = 255)
    private String failureMessage;

    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;

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
