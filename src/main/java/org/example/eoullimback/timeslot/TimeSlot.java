package org.example.eoullimback.timeslot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.Status;
import org.example.eoullimback.room.Room;

import java.time.LocalDateTime;

@Entity
@Table(name = "timeslots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_timeslots_room"))
    private Room room;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime emdTime;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int reserved;

    @Column(nullable = false)
    private Status status;

    @Builder
    public TimeSlot(Long id, Room room, LocalDateTime startTime, LocalDateTime emdTime, int capacity, int reserved, Status status) {
        this.id = id;
        this.room = room;
        this.startTime = startTime;
        this.emdTime = emdTime;
        this.capacity = capacity;
        this.reserved = reserved;
        this.status = status;
    }
}
