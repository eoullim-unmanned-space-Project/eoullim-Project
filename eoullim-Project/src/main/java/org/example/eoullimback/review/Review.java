package org.example.eoullimback.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.user_auth.user.User;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_review_once", columnNames = {"payment_id"})
        },
        indexes = {
                @Index(name = "idx_review_room", columnList = "room_id, rating")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", nullable = false)
    private Byte rating;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_reviews_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "fk_reviews_room_id"))
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_reviews_payment_id"))
    private Payment payment;

    @Builder
    public Review(
            Byte rating,
            String content,
            User user,
            Room room,
            Payment payment
    ) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.room = room;
        this.payment = payment;
    }
}
