package org.example.eoullimback.inquiry;

import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface InquiryChatService {
    SseEmitter connectInquiry(Long id);

    InquiryChatResponse.MessageDTO sendMessage(Long id, Long roomId, String message);

    List<InquiryChatResponse.MessageDTO> findAllByRoomId(Long roomId);

    void sendAdminReply(Long roomId, String message, Long id);
}
