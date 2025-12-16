package org.example.eoullimback.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.CapacityPolicy;
import org.example.eoullimback._common.enums.Category;
import org.example.eoullimback.place.Place;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rooms_place"))
    private Place place;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CapacityPolicy capacityPolicy;

    @Builder
    public Room(Long id, Place place, String name, String content, Category category, CapacityPolicy capacityPolicy) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.content = content;
        this.category = category;
        this.capacityPolicy = capacityPolicy;
    }
}
