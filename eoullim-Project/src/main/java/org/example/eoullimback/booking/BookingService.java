package org.example.eoullimback.booking;


import jakarta.validation.Valid;

public interface BookingService {
    Booking saveBooking(Long id, Long roomId, Long timeSlotId, BookingRequest.@Valid createDTO createDTO);
    BookingResponse.CalculateAmountDTO calculateAmount(BookingRequest.CalculateAmountDTO calculateAmountDTO);
}
