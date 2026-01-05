package org.example.eoullimback.booking_auto;

import org.example.eoullimback.booking.Booking;

import java.util.List;

public interface BookingSchedulerService {
    List<Booking> releaseSlot();
}
