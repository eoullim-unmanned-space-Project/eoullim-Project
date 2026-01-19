package org.example.eoullimback.booking_auto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.booking.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingSchedulerServiceImpl implements BookingSchedulerService{

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void releaseSlot() {
        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(5);
        List<Booking> bookingEntities = bookingRepository.findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, limitTime);

        if (bookingEntities.isEmpty()) {
            log.info("[ 만료된 부킹이 없습니다. ]");
            return;
        }

        for (Booking booking : bookingEntities) {
            if (booking.getTimeSlot() != null) {
                booking.getTimeSlot().open();
            }
            log.info("[ 부킹 삭제 예정 ] 부킹코드: {}", booking.getBookingCode());
        }

        bookingRepository.deleteAll(bookingEntities);
        log.info("[ {}개의 만료된 부킹이 삭제되었습니다. ]", bookingEntities.size());
    }
}
