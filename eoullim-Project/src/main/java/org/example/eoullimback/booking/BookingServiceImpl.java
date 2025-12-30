package org.example.eoullimback.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.room.RoomRepository;
import org.example.eoullimback.timeslot.TimeSlotRepository;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;


    @Override
    @Transactional
    public Booking saveBooking(Long id, Long roomId, Long timeSlotId, BookingRequest.@Valid createDTO createDTO) {


        return null;
    }
}
