package org.example.eoullimback.notification;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "notifications")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "qr_code", nullable = false, length = 255)
    private String qrCode;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_notification_user"))
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_notification_payment"))
//    private Payment payment;


}
