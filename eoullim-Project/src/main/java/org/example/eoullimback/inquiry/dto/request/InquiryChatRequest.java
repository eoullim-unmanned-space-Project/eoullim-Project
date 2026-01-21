package org.example.eoullimback.inquiry.dto.request;

import lombok.Data;

public class InquiryChatRequest {

    @Data
    public static class SendDTO {
        String message;

        public void validate() {
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalArgumentException("메시지는 필수입니다");
            }
        }
    }
}
