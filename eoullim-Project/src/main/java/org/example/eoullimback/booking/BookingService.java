package org.example.eoullimback.booking;


import jakarta.validation.Valid;
import org.example.eoullimback._common.enums.bookig.BookingStatus;
import org.example.eoullimback.user_auth.user.dto.response.UserResponse;

import java.util.List;

public interface BookingService {
    BookingResponse.CalculateAmountDTO calculateAmount(BookingRequest.CalculateAmountDTO calculateAmountDTO);
    String saveBooking(Long id, BookingRequest.createDTO createDTO);
    BookingResponse.DetailDTO detailBooking(Long id, String bookingCode);
    List<UserResponse.UserBookingDTO> searchBookings(Long id, String bookingCode, BookingStatus status);
    void cancelBooking(Long id, String bookingCode);
    BookingResponse.CountDTO countTodayBookings();
}
