package org.example.eoullimback.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.place.Place;

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
    private int maxCapacity;

    @Column(nullable = false, name = "default_price")
    private int defaultPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    private String roomImage;

    @Builder
    public Room(Long id, Place place, String name, String content,int maxCapacity, int defaultPrice, RoomStatus status,String roomImage) {
        this.id = id;
        this.place = place;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.defaultPrice = defaultPrice;
        this.content = content;
        this.status = status;
        this.roomImage = roomImage;
    }

    public void update(RoomRequest.UpdateDTO updateDTO) {

        this.name = updateDTO.name;
        this.content = updateDTO.content;
        this.maxCapacity = updateDTO.maxCapacity;
        this.status = updateDTO.status;
        this.defaultPrice = updateDTO.defaultPrice;
        this.roomImage = updateDTO.getRoomImageFileName();
    }

    public String getRoomFilePath() {
        if (this.roomImage == null) {
            return null;
        }
        if (this.roomImage.startsWith("http")) {
            return this.roomImage;
        }
        return "/images/" + this.roomImage;
    }
}