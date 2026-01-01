package org.example.eoullimback.booking;


import jakarta.validation.Valid;

public interface BookingService {
    BookingResponse.CalculateAmountDTO calculateAmount(BookingRequest.CalculateAmountDTO calculateAmountDTO);
    String saveBooking(Long id, BookingRequest.createDTO createDTO);
    BookingResponse.DetailDTO detailBooking(Long id, String bookingCode);
}
