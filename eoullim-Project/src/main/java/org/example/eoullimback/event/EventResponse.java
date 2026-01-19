package org.example.eoullimback.event;

import lombok.Data;
import java.time.LocalDate;

public class EventResponse {

    @Data
    public static class FortuneResultDTO {
        private int luckyScore;
        private String luckyItem;
        private String content;
    }

    @Data
    public static class DetailDTO {
       private String userName;
       private int luckyScore;
       private String luckyItem;
       private String content;
       private LocalDate fortuneDate;
       private Boolean alreadyExists = false;

        public DetailDTO(boolean alreadyExists) {
            this.alreadyExists = alreadyExists;
        }

        public DetailDTO(Event event) {
            this.userName = event.getUser().getName();
            this.luckyScore = event.getLuckyScore();
            this.luckyItem = event.getLuckyItem();
            this.content = event.getContent();
            this.fortuneDate = event.getFortuneDate();
            this.alreadyExists = true;
        }

    }
}
