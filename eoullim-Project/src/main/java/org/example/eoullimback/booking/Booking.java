package org.example.eoullimback.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.item.Item;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.user_auth.user.User;

import java.time.LocalDate;

@Entity
@Table(name = "bookings",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_timeslot_item", columnNames = {"user_id", "timeslot_id", "item_id"})
    },
    indexes = {
            @Index(
                    name = "idx_bookings_user_item",
                    columnList = "user_id, item_id"
            ),
            @Index(name = "idx_bookings_timeslot",
                columnList = "timeslot_id"
            )
   }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private int qty = 0;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
}
