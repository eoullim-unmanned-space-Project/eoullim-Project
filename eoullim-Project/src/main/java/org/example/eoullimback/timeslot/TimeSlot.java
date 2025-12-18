package org.example.eoullimback.timeslot;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.place.PlaceStatus;
import org.example.eoullimback.room.Room;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_slots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_time_slots_room"))
    private Room room;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaceStatus status;

    @Builder
    public TimeSlot(Long id, Room room, LocalDateTime startTime, LocalDateTime endTime, int capacity, PlaceStatus status) {
        this.id = id;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.status = status;
    }
}
