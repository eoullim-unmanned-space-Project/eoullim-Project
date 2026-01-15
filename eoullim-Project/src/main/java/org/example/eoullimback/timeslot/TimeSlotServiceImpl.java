package org.example.eoullimback.timeslot;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;
import org.example.eoullimback.item.Item;
import org.example.eoullimback.room.Room;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeSlotServiceImpl {

    private static final int CHUCK_SIZE = 500;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void createRoomInTimeSlots(Room saveRoom, LocalDate today) {

        List<TimeSlot> timeSlotList = new ArrayList<>();

        LocalDate endDate = today.plusDays(30);

        long daysBetween = ChronoUnit.DAYS.between(today, endDate);

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate targetDate = today.plusDays(i);

            for (int hour = 0; hour < 24; hour++) {
                LocalDateTime start = targetDate.atTime(hour, 0);
                LocalDateTime end = start.plusHours(1);

                TimeSlot timeSlot = TimeSlot.builder()
                        .room(saveRoom)
                        .slotMonth(targetDate.toString())
                        .startTime(start)
                        .endTime(end)
                        .capacity(saveRoom.getMaxCapacity())
                        .status(SlotStatus.OPEN)
                        .build();

                timeSlotList.add(timeSlot);

                if (timeSlotList.size() >= CHUCK_SIZE) {
                    persistTimeSlotsWithItems(timeSlotList);
                }
            }
        }

        if (!timeSlotList.isEmpty()) {
            persistTimeSlotsWithItems(timeSlotList);
        }
    }

    private void persistTimeSlotsWithItems(List<TimeSlot> timeSlotList) {

        for (TimeSlot timeSlot : timeSlotList) {

            em.persist(timeSlot);

            int price = calculatePrice(timeSlot.getRoom(), timeSlot.getStartTime().toLocalDate());

            Item item = Item.builder()
                    .timeSlot(timeSlot)
                    .price(price)
                    .build();

            em.persist(item);
        }
        em.flush();

        em.clear();;

        timeSlotList.clear();
    }

    private int calculatePrice(Room room, LocalDate date) {
            int base = room.getDefaultPrice();

            int weekendPrice = (int) (Math.round(base * 1.3 / 100) * 100);

            if (isWeekend(date)) {
                base += weekendPrice;
            }

            return base;
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() >= 5;
    }

}
