package org.example.eoullimback.timeslot;

import org.example.eoullimback.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Query("SELECT DISTINCT t.room.id FROM TimeSlot t WHERE t.slotMonth = :slotMonth ")
    List<Long> findExistedRoomIdByMonth(@Param("slotMonth") String slotMonth);

    void deleteAllByRoom(Room room);
}
