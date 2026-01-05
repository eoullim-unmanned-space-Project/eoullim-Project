package org.example.eoullimback.room_image;

import org.example.eoullimback.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    List<RoomImage> findAllByRoom(Room roomEntity);

    @Query("SELECT IFNULL(MAX(ri.displayOrder), 0) FROM RoomImage ri JOIN ri.room r WHERE r.id = :roomId")
    int findMaxDisplayOrder(@Param("roomId") Long roomId);

    Optional<List<RoomImage>> findByRoomIdOrderByDisplayOrderAsc(Long roomId);

    RoomImage findFirstByRoomId(Long roomId);
}
