package org.example.eoullimback.room;

import org.example.eoullimback._common.enums.room.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus roomStatus);

    boolean existsByRoomAndYearMonth(List<Room> roomsEntity, YearMonth nextMonth);
    void deleteByPlaceId(Long placeId);
}
