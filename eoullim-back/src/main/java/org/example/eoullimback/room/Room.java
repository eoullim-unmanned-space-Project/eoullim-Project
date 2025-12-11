package org.example.eoullimback.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback.common.enums.CapacityPolicy;
import org.example.eoullimback.common.enums.Category;
import org.example.eoullimback.place.Place;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "place_id", nullable = false,
//            foreignKey = @ForeignKey(name = "fk_rooms_place"))
//    private Place place;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 20)
    private CapacityPolicy capacityPolicy;

    public Room(Long id, String name, String content, Category category, CapacityPolicy capacityPolicy) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.category = category;
        this.capacityPolicy = capacityPolicy;
    }
}
