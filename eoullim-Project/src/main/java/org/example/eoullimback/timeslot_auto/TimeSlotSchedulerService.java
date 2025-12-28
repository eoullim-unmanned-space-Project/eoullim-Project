package org.example.eoullimback.timeslot_auto;

import java.time.YearMonth;

public interface TimeSlotSchedulerService {
    void createNextMonthTimeSlot(YearMonth nextMonth);
}
