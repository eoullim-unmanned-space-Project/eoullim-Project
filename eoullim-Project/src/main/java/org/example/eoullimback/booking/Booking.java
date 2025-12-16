package org.example.eoullimback.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.item.Item;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.user_auth.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_timeslot_item", columnNames = {"user_id", "timeslot_id", "item_id"})
    },
    indexes = {
            @Index(
                    name = "idx_bookings_user",
                    columnList = "user_id, status"
            ),
            @Index(name = "idx_bookings_timeslot",
                columnList = "timeslot_id"
            )
   }
)
@NoArgsConstructor
@Getter
public class Booking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeslot_id", nullable = false)
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Builder
    public Booking (User user, TimeSlot timeSlot, Item item, int qty, Long amount, LocalDate bookingDate, BookingStatus status) {

        if (qty <= 0) {
            throw new IllegalArgumentException("예약 수량은 0보다 커야합니다.");
        }

        this.user = user;
        this.timeSlot = timeSlot;
        this.item = item;
        this.qty = qty;
        this.amount = amount;
        this.bookingDate = (bookingDate != null) ? bookingDate : LocalDate.now();
        this.status = (status != null) ? status : BookingStatus.PENDING;
    }

    public void markCanceled() {
        if (this.status != BookingStatus.CANCELED && this.status != BookingStatus.REFUNDED ) {
            this.cancelledAt = LocalDateTime.now();
            this.status = BookingStatus.CANCELED;
        }
    }
    public void markRefund() {
        if (this.status != BookingStatus.CANCELED && this.status != BookingStatus.REFUNDED) {
            this.cancelledAt = LocalDateTime.now();
            this.status = BookingStatus.REFUNDED;
        }
    }
}
