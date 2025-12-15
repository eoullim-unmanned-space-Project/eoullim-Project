package org.example.eoullimback.payment;

import jakarta.persistence.*;
import lombok.*;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.payment.PaymentMethod;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.user_auth.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payments_payment_key", columnNames = "payment_key"),
                @UniqueConstraint(name = "uk_payments_booking_id", columnNames = "booking_id")
        },
        indexes = {
                @Index(name = "idx_payments_user_id", columnList = "user_id"),
                @Index(name = "idx_payments_order_id", columnList = "order_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", updatable = false)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "booking_id", nullable = false)
        private Booking booking;

        @Column(nullable = false, length = 100)
        private String orderId;

        @Column(nullable = false, length = 100, unique = true)
        private String paymentKey;

        @Column(nullable = false)
        private Long amount;

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false, length = 30)
        private PaymentMethod method;

        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false, length = 30)
        private PaymentStatus status;

        @Column(nullable = false, length = 50)
        private String productCode;

        @Column(nullable = false, length = 100)
        private String productName;

        @Column(nullable = true, length = 50)
        private String failureCode;

        @Column(nullable = true, length = 255)
        private String failureMessage;

        @CreationTimestamp
        @Column(name = "requested_at", nullable = false, updatable = false)
        private LocalDateTime requestedAt;

        private LocalDateTime approvedAt;
        private LocalDateTime cancelledAt;


        @Builder
        public Payment(User user, Booking booking, String orderId, String paymentKey, Long amount, PaymentMethod method, PaymentStatus status ,String productCode, String productName) {
            this.user = user;
            this.booking = booking;
            this.orderId = orderId;
            this.paymentKey = paymentKey;
            this.amount = amount;
            this.method = (method != null) ? method : PaymentMethod.MOCK;
            this.status = (status != null) ? status : PaymentStatus.READY;
            this.productCode = productCode;
            this.productName = productName;
        }

        public void markSuccess() {
                this.status = PaymentStatus.SUCCESS;
                this.approvedAt = LocalDateTime.now();
                this.failureCode = null;
                this.failureMessage = null;
        }

        public void markFailed(String failureCode, String failureMessage) {
                this.status = PaymentStatus.FAILED;
                this.failureCode = failureCode;
                this.failureMessage = failureMessage;
        }

        public void markRefunded() {
                this.status = PaymentStatus.REFUNDED;
                this.cancelledAt = LocalDateTime.now();
        }
}