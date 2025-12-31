package org.example.eoullimback.timeslot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.room.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "time_slots",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_room_slot_month_start_time", columnNames = {"room_id", "slot_month", "start_time"})
        },
        indexes = {
            @Index(
                    name = "idx_time_slots_slot_month",
                    columnList = "slot_month"
            ),
            @Index(
                    name = "idx_time_slots_start_time",
                    columnList = "start_time"
            ),
            @Index(
                    name = "idx_time_slots_end_time",
                    columnList = "end_time"
            )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_time_slots_room"))
    private Room room;

    @Column(nullable = false, length = 7)
    private String slotMonth;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Builder
    public TimeSlot(Long id, Room room, String slotMonth,  LocalDateTime startTime, LocalDateTime endTime, int capacity, RoomStatus status) {
        this.id = id;
        this.room = room;
        this.slotMonth = slotMonth;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.status = status;
    }
}
