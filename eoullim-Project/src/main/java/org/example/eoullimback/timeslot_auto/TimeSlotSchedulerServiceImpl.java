package org.example.eoullimback.timeslot_auto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.room.RoomStatus;
import org.example.eoullimback._common.enums.time_slot.SlotStatus;
import org.example.eoullimback.item.Item;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRepository;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.timeslot.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotSchedulerServiceImpl implements TimeSlotSchedulerService {

    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;

    private static final int WEEKEND_PRICE = 10000;
    private static final int CHUNK_SIZE = 500;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void createNextMonthTimeSlot(YearMonth nextMonth) {
        List<Room> rooms = roomRepository.findByStatus(RoomStatus.OPEN);
        List<Long> existRoomIds = timeSlotRepository.findExistedRoomIdByMonth(nextMonth.toString());

        LocalDate startDate = nextMonth.atDay(1);
        LocalDate endDate = nextMonth.atEndOfMonth();

        int counter = 0;

        for (Room room : rooms) {
            if (existRoomIds.contains(room.getId())) continue;

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                for (int hour = 0; hour < 24; hour++) {
                    LocalDateTime start = date.atTime(hour, 0);
                    LocalDateTime end = start.plusHours(1);

                    TimeSlot timeSlot = TimeSlot.builder()
                            .room(room)
                            .slotMonth(nextMonth.toString())
                            .startTime(start)
                            .endTime(end)
                            .capacity(room.getMaxCapacity())
                            .status(SlotStatus.OPEN)
                            .build();

                    em.persist(timeSlot);
                    em.flush();

                    int price = calculatePrice(room, date);

                    Item item = Item.builder()
                            .timeSlot(timeSlot)
                            .price(price)
                            .build();

                    em.persist(item);

                    if (++counter % CHUNK_SIZE == 0) {
                        em.flush();
                        em.clear();
                    }
                }
            }
        }

        em.flush();
        em.clear();
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private int calculatePrice(Room room, LocalDate date) {
        int base = room.getDefaultPrice();
        if (isWeekend(date)) {
            base += WEEKEND_PRICE;
        }
        return base;
    }

    @Override
    @Transactional
    public void releaseHoldSlots() {

        List<TimeSlot> timeSlotEntities = timeSlotRepository.findAllByStatusHold(SlotStatus.HOLD, LocalDateTime.now());

        timeSlotEntities.forEach(
                TimeSlot::open
        );
    }
}


