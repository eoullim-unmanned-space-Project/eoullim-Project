package org.example.eoullimback.qaa;

import lombok.Data;
import org.example.eoullimback.user_auth.user.User;

public class QaaRequest {

    @Data
    public static class SaveDto {
        private String title;
        private String content;
        private Long viewCount;
        private User user;

        public Qaa toEntity(User user) {
            return new Qaa(title, content, viewCount, user);
        }
    }

    @Data
    public static class UpdateDto {
        private String title;
        private String content;

        public void validate() {
            if(title == null || title.trim().isEmpty()) {
                throw new RuntimeException("제목은 필수 입니다.");
            }

            if(content == null || content.trim().isEmpty()) {
                throw new RuntimeException("내용은 필수 입니다.");
            }
        }
    }
}
