package org.example.eoullimback.inquiry.dto.response;

import lombok.Data;
import org.example.eoullimback._common.enums.inquiry.SenderType;
import org.example.eoullimback.inquiry.InquiryChat;

import java.time.LocalDateTime;

public class InquiryChatResponse {

    @Data
    public static class MessageDTO {
       private Long roomId;
       private String sender;
       private String receiver;
       private String message;
       private SenderType senderType;
       private LocalDateTime createdAt;
       private String displayType;

       public MessageDTO(InquiryChat inquiryChat) {
            this.roomId = inquiryChat.getRoom().getId();
            this.sender = inquiryChat.getSender();
            this.receiver = inquiryChat.getReceiver();
            this.message = inquiryChat.getMessage();
           this.senderType = inquiryChat.getSenderType();

            this.displayType = switch (this.senderType) {
                case USER -> "사용자";
                case ADMIN -> "관리자";
            };
       }
    }
}
