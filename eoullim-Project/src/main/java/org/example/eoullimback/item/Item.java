package org.example.eoullimback.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.timeslot.TimeSlot;

@Entity
@Table(name = "items",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_items_title", columnNames = "title")

        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_items_time_slot"))
    private TimeSlot timeSlot;

    @Column(nullable = false, length = 50, unique = true)
    private String title;

    @Column(nullable = false, length = 150)
    private String context;

    @Column(nullable = false)
    private int price;

    @Builder
    public Item(Long id, TimeSlot timeSlot, String title, String context, int price) {
        this.id = id;
        this.timeSlot = timeSlot;
        this.title = title;
        this.context = context;
        this.price = price;
    }
}
