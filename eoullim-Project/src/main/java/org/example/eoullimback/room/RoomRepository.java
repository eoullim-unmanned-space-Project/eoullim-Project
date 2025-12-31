package org.example.eoullimback.room;

import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus roomStatus);

    @Query("""
        SELECT r FROM Room r JOIN FETCH r.place WHERE r.id = :roomId
    """)
    Optional<Room> findByWithPlace(@Param("roomId") Long roomId);

    void deleteByPlaceId(Long placeId);
}
