package org.example.eoullimback.item.dto.response;

import org.example.eoullimback.item.Item;
import org.example.eoullimback.item.dto.request.SaveItemRequestDTO;
import org.example.eoullimback.timeslot.TimeSlot;

public record SaveItemResponseDTO(
        TimeSlot timeSlot,
        String title,
        int price
) {
    public static SaveItemRequestDTO from(Item item) {
        return new SaveItemRequestDTO(
                item.getTimeSlot(),
                item.getTitle(),
                item.getPrice()
        );
    }
}
