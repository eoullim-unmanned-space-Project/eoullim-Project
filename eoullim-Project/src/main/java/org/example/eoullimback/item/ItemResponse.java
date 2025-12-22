package org.example.eoullimback.item;

import lombok.Data;
import org.example.eoullimback.timeslot.TimeSlot;

public class ItemResponse {

    @Data
    public static class ItemDTO {
        private TimeSlot timeSlot;
        private int price;

        public ItemDTO(Item item) {
            this.timeSlot = item.getTimeSlot();
            this.price = item.getPrice();
        }
    }
}
