package org.example.eoullimback.timeslot;

import jakarta.persistence.LockModeType;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;
import org.example.eoullimback.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Query("SELECT DISTINCT t.room.id FROM TimeSlot t WHERE t.slotMonth = :slotMonth ")
    List<Long> findExistedRoomIdByMonth(@Param("slotMonth") String slotMonth);

    void deleteAllByRoom(Room room);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TimeSlot t WHERE t.id IN :timeSlotIds")
    List<TimeSlot> findAllByWithLock(@Param("timeSlotIds") List<Long> timeSlotIds);

    @Query("SELECT t FROM TimeSlot t WHERE t.status = :slotStatus AND t.holdExpiredAt < :now")
    List<TimeSlot> findAllByStatusHold(@Param("slotStatus")SlotStatus slotStatus, @Param("now")LocalDateTime now);
}
