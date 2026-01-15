package org.example.eoullimback.booking;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRepository;
import org.example.eoullimback.timeslot.SseTimeSlotService;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.timeslot.TimeSlotRepository;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;
    private final SseTimeSlotService sseTimeSlotService;

    @Override
    @Transactional
    public String saveBooking(Long id, BookingRequest.createDTO createDTO) {

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        Room roomEntity = roomRepository.findById(createDTO.getRoomId())
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        List<TimeSlot> timeslotEntities = timeSlotRepository.findAllByWithLock(createDTO.getTimeSlotIds());

        if (timeslotEntities.size() != createDTO.getTimeSlotIds().size()) {
            throw new Exception404(ErrorCode.TIMESLOT_NOT_FOUND);
        }

        for (TimeSlot timeSlot : timeslotEntities) {
            if (!timeSlot.isOpen()) {
                throw new Exception400(ErrorCode.ALREADY_TIMESLOT);
            }

            timeSlot.hold(LocalDateTime.now().plusMinutes(5));
        }

        String bookingCode = "bk_" + userEntity.getId() + "_" + UUID.randomUUID().toString().substring(0, 8);

        List<Booking> bookingList = new ArrayList<>();

        Long slotAmount = createDTO.getTotalAmount() / timeslotEntities.size();
        Long remainder = createDTO.getTotalAmount() % timeslotEntities.size();


        for (int i = 0; i < timeslotEntities.size(); i++) {
            TimeSlot timeSlot = timeslotEntities.get(i);

            Long perSlotAmount = slotAmount;
            if (i == 0) {
                perSlotAmount += remainder;
            }

            Booking booking = createDTO.toEntity(userEntity, roomEntity, timeSlot, bookingCode, perSlotAmount, timeSlot.getStartTime().toLocalDate());

            bookingList.add(booking);
        }

        bookingRepository.saveAll(bookingList);

        sseTimeSlotService.timeSlotBroadcast(roomEntity.getId(), timeslotEntities);

        return bookingCode;
    }

    @Override
    public BookingResponse.CalculateAmountDTO calculateAmount(BookingRequest.CalculateAmountDTO calculateAmountDTO) {

       Room roomEntity = roomRepository.findById(calculateAmountDTO.getRoomId())
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

       int defaultPrice = roomEntity.getDefaultPrice();

       int slotCount = calculateAmountDTO.getTimeSlotIds().size();

       long amount = (long) defaultPrice * slotCount;

        return new BookingResponse.CalculateAmountDTO(amount);
    }

    @Override
    public BookingResponse.DetailDTO detailBooking(Long id, String bookingCode) {

        List<Booking> bookingEntities = bookingRepository.findDetailByBookingCodeAndUser(id, bookingCode)
                .orElseThrow(() -> new Exception404(ErrorCode.BOOKING_NOT_FOUND));

        return new BookingResponse.DetailDTO(bookingEntities);
    }

    @Override
    public List<UserResponse.UserBookingDTO> searchBookings(Long id, String bookingCode, BookingStatus status) {

        String code = (bookingCode == null || bookingCode.isEmpty()) ? null : bookingCode;

        List<Booking> bookingEntities =  bookingRepository.findAllByUserIdAndBookingCodeAndStatus(id, code, status);

        if (bookingEntities.isEmpty()) {
            UserResponse.UserBookingDTO.empty();
            return Collections.emptyList();
        }

        return bookingEntities.stream()
                .collect(Collectors.groupingBy(Booking::getBookingCode))
                .values()
                .stream()
                .map(UserResponse.UserBookingDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public void cancelBooking(Long userId, String bookingCode) {

       List<Booking> bookingEntities = bookingRepository.findAllByBookingCodeWithUser(bookingCode, userId);

       if (bookingEntities.isEmpty()) {
           throw new Exception404(ErrorCode.BOOKING_NOT_FOUND);
       }

       for (Booking booking : bookingEntities) {
           booking.changeCanceled();

           if (booking.getTimeSlot() != null) {
               booking.getTimeSlot().open();
           }
       }


    }

    @Override
    public BookingResponse.CountDTO countTodayBookings() {

        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        Long todayCount = bookingRepository.countTodayBookings(today);
        Long yesterdayCount = bookingRepository.countYesterdayBookings(today, yesterday);

        return new BookingResponse.CountDTO(todayCount, yesterdayCount);
    }
}
