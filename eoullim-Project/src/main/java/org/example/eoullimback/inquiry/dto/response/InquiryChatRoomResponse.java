package org.example.eoullimback.inquiry.dto.response;

import lombok.Data;
import org.example.eoullimback.inquiry.InquiryChatRoom;

import java.time.LocalDateTime;
import java.util.List;

public class InquiryChatRoomResponse {

    @Data
    public static class ListDTO {
        private Long chatRoomId;
        private String userName;
        private String lastMessage;
        private LocalDateTime createdAt;

        public ListDTO(InquiryChatRoom room, String lastMessage) {
            this.chatRoomId = room.getId();
            this.userName = room.getUser().getName();
            this.lastMessage = lastMessage;
            this.createdAt = room.getCreatedAt();
        }
    }

    @Data
    public static class CreateResponse {
        private Long chatRoomId;
        private String userName;
        private String adminName;
        private LocalDateTime createdAt;
        private boolean isRead;

        public CreateResponse(InquiryChatRoom inquiryChatRoom) {
            this.chatRoomId = inquiryChatRoom.getId();
            this.userName = inquiryChatRoom.getUser().getName() ;
            this.adminName = inquiryChatRoom.getAdmin() != null ? inquiryChatRoom.getAdmin().getName() : null;
            this.createdAt = inquiryChatRoom.getCreatedAt();

        }
    }

}
