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

    // 주말에 더 받을 추가 금액
    private static final int WEEKEND_PRICE = 5000;
    
    // 인원 체크 -> 추후 room(방)에 옮길 예정
    private static final int DEFAULT_CAPACITY = 6;

    // 배치 사이즈
    private static final int CHUCK_SIZE = 500;

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public void createNextMonthTimeSlot(YearMonth nextMonth) {

        List<Room> roomsEntity = roomRepository.findByStatus(RoomStatus.OPEN);

        List<Long> existRoomIds = timeSlotRepository.findExistedRoomIdByMonth(nextMonth.toString());

        List<TimeSlot> timeSlotList = new ArrayList<>();

        // 1번째 날
        LocalDate startDate = nextMonth.atDay(1);
        
        // 마지막 날
        LocalDate endDate = nextMonth.atEndOfMonth();

        for (Room room : roomsEntity) {

            // 방어적 코드 roomIds에 가져온 room.id를 포함하고 있다면 무시
            if (existRoomIds.contains(room.getId())) {
                continue;
            }

            // 방어적 코드 가져온 상태 값이 open이 아니라면 무시
            if (room.getStatus() != RoomStatus.OPEN) {
                continue;
            }

            // 달을 처리하는 로직
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

                // for문을 돌리는 0 ~ 24시간까지
                for (int hour = 0; hour < 24; hour++) {
                    LocalDateTime start = date.atTime(hour, 0);
                    LocalDateTime end = start.plusHours(1);

                    TimeSlot timeSlot = TimeSlot.builder()
                            .room(room)
                            .slotMonth(nextMonth.toString())
                            .startTime(start)
                            .endTime(end)
                            .capacity(DEFAULT_CAPACITY)
                            .status(SlotStatus.OPEN)
                            .build();

                    // 배치용 임시 List -> 서류 묶음
                    // size를 통해서 배치를 계산한다
                    timeSlotList.add(timeSlot);

                    if (timeSlotList.size() >= CHUCK_SIZE) {
                        persistTimeSlotsWithItems(timeSlotList);
                    }
                }
            }
        }

        // 지금 배치 사이즈를 500을 줬기 때문에 500개가 다 찬 박스만 정리 그렇기 때문에
        // if문으로 나머지 남은 값을 처리해줘야한다.
        if (!timeSlotList.isEmpty()) {
            persistTimeSlotsWithItems(timeSlotList);
        }
    }

    private void persistTimeSlotsWithItems(List<TimeSlot> timeSlotList) {
        // 타임슬롯에 담아놓은 .add로
        // forEach문으로 반복문 처리
        for (TimeSlot timeSlot : timeSlotList) {
            // 영속성 1차 컨텍스트에 쌓아놓기
            em.persist(timeSlot);

            // 주말 + 카테고리별 계산을 처리하는 함수를 통해서 토탈 가격을 받아서 처리해준다.
            int price = calculatePrice(timeSlot.getRoom(), timeSlot.getStartTime().toLocalDate());

            Item item = Item.builder()
                    .timeSlot(timeSlot)
                    .price(price)
                    .build();

            // item을 1차 캐시에 올려둔다
            em.persist(item);
        }

        // .flush를 통해서 쌓아둔 값을 전송 ->
        em.flush();

        // .flush를 한 뒤에는 .clear로 값을 깨끗하게 비워준다.
        em.clear();

        // .timeSlot을 담은 buffer도 값을 비워준다.
        timeSlotList.clear();
    }

    private boolean isWeekend(LocalDate date) {
        // 1(월) 2(화) 3(수) 4(목)
        // (5)금, (6)토, (7)일 5보다 크다면 주말
        // 금 토 일 가격을 더 붙여서 받을 예정
        return date.getDayOfWeek().getValue() >= 5;
    }

    private int calculatePrice(Room room, LocalDate date) {
        // room의 초기 값을 변수에 담아준다.
        int base = room.getDefaultPrice();

        if (isWeekend(date)) {
            base = base + WEEKEND_PRICE;
        }

        return base;
    }
}


