package org.example.eoullimback.timeslot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback.item.Item;
import org.example.eoullimback.room.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class TimeSlot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_time_slots_room"))
    private Room room;

    @Column(nullable = false, columnDefinition = "CHAR(7)")
    private String slotMonth;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @Column(name = "holdexpired_at")
    private LocalDateTime holdExpiredAt;

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @Builder
    public TimeSlot(Long id, Room room, String slotMonth,  LocalDateTime startTime, LocalDateTime endTime, int capacity, SlotStatus status) {
        this.id = id;
        this.room = room;
        this.slotMonth = slotMonth;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.status = status;
    }

    public boolean isOpen() {
        return this.status ==  SlotStatus.OPEN;
    }

    public void close() {
        if (this.status == SlotStatus.CLOSED) {
            throw new Exception400(ErrorCode.ALREADY_BOOKED_TIME_SLOT);
        }

        this.status = SlotStatus.CLOSED;
        this.holdExpiredAt = null;
    }

    public void open() {
        this.status = SlotStatus.OPEN;
        this.holdExpiredAt = null;
    }

    public void hold(LocalDateTime holdExpiredAt) {
        this.status = SlotStatus.HOLD;
        this.holdExpiredAt = holdExpiredAt;
    }


}
