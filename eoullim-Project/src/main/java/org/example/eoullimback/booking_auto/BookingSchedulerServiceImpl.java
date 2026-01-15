package org.example.eoullimback.booking_auto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.enums.payment.PaymentStatus;
import org.example.eoullimback.booking.Booking;
import org.example.eoullimback.booking.BookingRepository;
import org.example.eoullimback.payment.Payment;
import org.example.eoullimback.payment.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingSchedulerServiceImpl implements BookingSchedulerService{

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

//    @Override
//    @Transactional
//    public List<Booking> releaseSlot() {
//
//        // 생성된 시각에 -5분을 빼서 값을 구한다.
//        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(5);
//
//        // 상태값이 대기중인 부킹을 가져온다.
//        List<Booking> bookingEntities = bookingRepository.findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, limitTime);
//
//        if (bookingEntities.isEmpty()) {
//            log.info("[ 만료된 부킹이 없습니다. ]");
//            return bookingEntities;
//        }
//
//        // 반복문을 돌려서 취소 및 상태값 변경
//        for (Booking booking : bookingEntities) {
//            booking.changeCanceled();
//
//            if (booking.getTimeSlot() != null) {
//                booking.getTimeSlot().open();
//            }
//
//            log.info("[ 부킹 상태가 변경되었습니다. ] 부킹코드: {}, 부킹상태: {}", booking.getBookingCode(), booking.getStatus());
//        }
//
//
//        List<Payment> paymentEntities = paymentRepository.findAllPaymentInBookings(bookingEntities);
//
//        for (Payment payment : paymentEntities) {
//            payment.markFailed("SCHEDULER_CANCELED", "5분안에 예약하지 않아 스케줄러에 의해 취소되었습니다.");
//            log.info("[ 결제 상태가 변경되었습니다. ] 결제코드: {}, 결제상태: {}", payment.getFailureCode(), payment.getFailureMessage());
//        }
//
//        return bookingEntities;
//    }

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
