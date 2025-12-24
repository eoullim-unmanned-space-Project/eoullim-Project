package org.example.eoullimback.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.file.RoomFile;
import org.example.eoullimback.place.Place;

import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rooms_place"))
    private Place place;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int defaultPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Builder
    public Room(Long id, Place place, String name, String content, int defaultPrice, RoomStatus status) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.defaultPrice = defaultPrice;
        this.content = content;
        this.status = status;
    }

    public void update(RoomRequest.UpdateDTO updateDTO) {

        updateDTO.validate();

        this.name = updateDTO.name;
        this.content = updateDTO.content;
        this.status = updateDTO.status;
        this.defaultPrice = updateDTO.defaultPrice;
    }
}