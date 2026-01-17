package org.example.eoullimback.event;

import jakarta.persistence.Column;
import lombok.Data;
import org.example.eoullimback.user_auth.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

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
       private Date fortuneDate;

        public DetailDTO(Event event) {
            this.userName = event.getUser().getName();
            this.luckyScore = event.getLuckyScore();
            this.luckyItem = event.getLuckyItem();
            this.content = event.getContent();
            this.fortuneDate = event.getFortuneDate();
        }

    }
}
