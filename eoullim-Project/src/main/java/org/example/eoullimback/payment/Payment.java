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
                @UniqueConstraint(name = "uk_payments_payment_key", columnNames = "payment_key")
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

        // bookingCode -> bk_1_23425235
        @Column(nullable = false, length = 100)
        private String orderId;

        // 페이먼트 키 -> payment-1_1767432671179_b8b01f74
        @Column(nullable = false, length = 100, unique = true)
        private String paymentKey;

        @Column(unique = true)
        private String impUid;

        // 총 가격
        @Column(nullable = false)
        private Long amount;

        // 타입
        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false, length = 30)
        private PaymentMethod method;

        // 상태
        @Enumerated(value = EnumType.STRING)
        @Column(nullable = false, length = 30)
        private PaymentStatus status;

        // 룸 이름
        @Column(nullable = false, length = 100)
        private String productName;

        // 실패 코드
        @Column(nullable = true, length = 50)
        private String failureCode;

        // 실패 메세지
        @Column(nullable = true, length = 255)
        private String failureMessage;

        // 대기시간
        @CreationTimestamp
        @Column(name = "requested_at", nullable = false, updatable = false)
        private LocalDateTime requestedAt;

        private LocalDateTime approvedAt;
        private LocalDateTime cancelledAt;


        @Builder
        public Payment(User user, Booking booking, String orderId, String paymentKey, Long amount, PaymentMethod method, PaymentStatus status, String productName) {
            this.user = user;
            this.booking = booking;
            this.orderId = orderId;
            this.paymentKey = paymentKey;
            this.amount = amount;
            this.method = (method != null) ? method : PaymentMethod.MOCK;
            this.status = (status != null) ? status : PaymentStatus.READY;
            this.productName = productName;
        }

        public void markSuccess(String impUid) {
                this.status = PaymentStatus.SUCCESS;
                this.approvedAt = LocalDateTime.now();
                this.impUid = impUid;
                this.failureCode = null;
                this.failureMessage = null;
        }

        public void markFailed(String failureCode, String failureMessage) {
                this.status = PaymentStatus.FAILED;
                this.failureCode = failureCode;
                this.cancelledAt = LocalDateTime.now();
                this.failureMessage = failureMessage;
        }

        public void markRefunded() {
                    this.status = PaymentStatus.REFUNDED;
                    this.cancelledAt = LocalDateTime.now();
            }
}