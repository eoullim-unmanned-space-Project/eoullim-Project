package org.example.eoullimback.booking;

import jakarta.validation.Valid;
import org.example.eoullimback.user_auth.user.User;

public interface BookingService {
    Booking saveBooking(Long id, Long roomId, Long timeSlotId, BookingRequest.@Valid createDTO createDTO);
}
