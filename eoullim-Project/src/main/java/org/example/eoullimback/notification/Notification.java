package org.example.eoullimback.notification;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.notification.NotificationStatus;
import org.example.eoullimback._common.enums.notification.NotificationType;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "notifications")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NotificationStatus status;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "qr_code", nullable = false, length = 40)
    private String qrCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_notification_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_notification_payment"))
    private Payment payment;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @Builder
    public Notification(NotificationType type, NotificationStatus status, String message, String qrCode, User user, Payment payment, LocalDateTime createdAt, LocalDateTime sentAt) {
        this.type = type;
        this.status = status;
        this.message = message;
        this.qrCode = qrCode;
        this.user = user;
        this.payment = payment;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
    }
}
