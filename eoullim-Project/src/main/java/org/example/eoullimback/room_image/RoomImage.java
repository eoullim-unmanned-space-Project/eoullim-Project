package org.example.eoullimback.room_image;

import jakarta.persistence.*;
import lombok.*;
import org.example.eoullimback.room.Room;

@Entity
@Table(name = "room_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String filePath;

    private int displayOrder;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "fk_room_images_room"))
    private Room room;

    @Builder
    public RoomImage(String contentType, int displayOrder, String filePath, Long fileSize, String originalName, Room room, String storedName) {
        this.contentType = contentType;
        this.displayOrder = displayOrder;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.originalName = originalName;
        this.room = room;
        this.storedName = storedName;
    }

    public boolean checkOwner(Long roomId) {
        return this.room.getId().equals(roomId);
    }
}
