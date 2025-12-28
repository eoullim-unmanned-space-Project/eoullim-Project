package org.example.eoullimback.timeslot_auto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeSlotScheduler {

    private final TimeSlotSchedulerService timeSlotSchedulerService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void createNextMonthTimeSlot() {
        YearMonth nextMonth = YearMonth.now().plusMonths(1);

        log.info("[타임슬롯 스케줄러가 실행됩니다.] : , {}", nextMonth);

        try {
            timeSlotSchedulerService.createNextMonthTimeSlot(nextMonth);
            log.info("[ 타임슬롯이 성공적으로 생성 되었습니다. ] : {}", nextMonth);
        } catch (Exception e) {
            log.error("[ 타임슬롯을 생성하는데 실패했습니다. 확인해주세요. ] : {} ", nextMonth, e);

        }
    }
}
