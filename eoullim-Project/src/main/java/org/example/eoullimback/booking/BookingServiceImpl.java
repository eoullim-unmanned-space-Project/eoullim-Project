package org.example.eoullimback.booking;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRepository;
import org.example.eoullimback.timeslot.TimeSlot;
import org.example.eoullimback.timeslot.TimeSlotRepository;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public String saveBooking(Long id, BookingRequest.createDTO createDTO) {

        // 사용자 있는지
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        // 룸이 있는지
        Room roomEntity = roomRepository.findById(createDTO.getRoomId())
                .orElseThrow(() -> new Exception404(ErrorCode.ROOM_NOT_FOUND));

        // 타임슬롯이 있는지 for문 돌려야할것
        List<TimeSlot> timeslotEntities = timeSlotRepository.findAllById(createDTO.getTimeSlotIds());

        // 엔티티즈의 사이즈랑 받아온 사이즈가 다르다면 NOT FOUND
        if (timeslotEntities.size() != createDTO.getTimeSlotIds().size()) {
            throw new Exception404(ErrorCode.TIMESLOT_NOT_FOUND);
        }

        // 예약된 타임슬롯이 있다면 예외처리
        for (TimeSlot timeSlot : timeslotEntities) {
            timeSlot.close();
        }

        // 예약 코드를 생성
        String bookingCode = "bk_" + userEntity.getId() + "_" + UUID.randomUUID().toString().substring(0, 8);

        // 토탈 프라이스 쪼개서 넣기
        Long perSlotAmount = createDTO.getTotalAmount() / timeslotEntities.size();

        // 부킹 생성
        List<Booking> bookings = timeslotEntities.stream()
                .map(timeSlot -> createDTO.toEntity(userEntity, roomEntity, timeSlot, bookingCode, perSlotAmount))
                .toList();

        bookingRepository.saveAll(bookings);

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

        // user의 값을 담아서 같이 검증해준다
        List<Booking> bookingEntities = bookingRepository.findDetailByBookingCodeAndUser(id, bookingCode)
                .orElseThrow(() -> new Exception404(ErrorCode.BOOKING_NOT_FOUND));

        return new BookingResponse.DetailDTO(bookingEntities);
    }
}
