package org.example.eoullimback.booking_auto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback.booking.Booking;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingScheduler {

    private final BookingSchedulerService bookingSchedulerService;

    // 5분 마다 예약 상태 검증
//    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Scheduled(cron = "5 * * * * *")
    public void releaseSlot() {
        try {
            log.info("만료된 펜딩 슬롯을 해체하는 스케줄러를 시작합니다. 시간: {}", LocalDateTime.now());
            bookingSchedulerService.releaseSlot();


        } catch (Exception e) {
            log.error("부킹의 상태를 바꾸는데 실패했습니다. {}", e.getMessage(), e);
        }
    }
}
