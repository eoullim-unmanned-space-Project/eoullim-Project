package org.example.eoullimback.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.base.BaseTimeEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_items_time_slot"))
    private TimeSlot timeSlot;

    @Column(nullable = false)
    private int price;

    @Builder
    public Item(Long id, TimeSlot timeSlot, int price) {
        this.id = id;
        this.timeSlot = timeSlot;
        this.price = price;
    }
}
