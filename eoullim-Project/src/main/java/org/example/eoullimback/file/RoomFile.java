package org.example.eoullimback.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback.room.Room;

@Entity
@Table(name = "room_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_room_files_place"))
    private Room room;

    @Builder
    public RoomFile(Long id, Room room) {
        this.id = id;
        this.room = room;
    }
}
