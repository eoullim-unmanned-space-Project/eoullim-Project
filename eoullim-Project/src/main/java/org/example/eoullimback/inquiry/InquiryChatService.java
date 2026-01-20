package org.example.eoullimback.inquiry;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface InquiryChatService {
    SseEmitter connect(String clientId, Long roomId);

    void sendMessage(InquiryChatRoom room, String sender, String receiver, String message);

    List<InquiryChat> getInquiryChats(InquiryChatRoom room);
}
