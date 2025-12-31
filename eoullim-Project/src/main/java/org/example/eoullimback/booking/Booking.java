package org.example.eoullimback.booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_room_time_slot", columnNames = {"room_id", "time_slot_id"})
    },
    indexes = {
            @Index(
                    name = "idx_bookings_user",
                    columnList = "user_id, status"
            ),
            @Index(name = "idx_bookings_time_slot",
                columnList = "time_slot_id"
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
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bookings_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bookings_room"))
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "time_slot_id", nullable = false, foreignKey = @ForeignKey(name = "fk_bookings_time_slot"))
    private TimeSlot timeSlot;

    @Column(nullable = false)
    private Long itemSnapshotPrice;

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false)
    @Min(100)
    private Long amount;

    @Column(nullable = false)
    private LocalDate bookingDate;

    private LocalDateTime cancelledAt;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Builder
    public Booking (User user, TimeSlot timeSlot, Room room, Long itemSnapshotPrice, int qty, Long amount, LocalDate bookingDate, BookingStatus status) {

        if (qty <= 0) {
            throw new IllegalArgumentException("예약 수량은 0보다 커야합니다.");
        }

        this.user = user;
        this.timeSlot = timeSlot;
        this.room = room;
        this.itemSnapshotPrice = itemSnapshotPrice;
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
