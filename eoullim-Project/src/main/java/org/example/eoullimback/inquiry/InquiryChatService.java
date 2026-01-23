package org.example.eoullimback.inquiry;

import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface InquiryChatService {
    SseEmitter connectInquiry(Long roomId);

    InquiryChatResponse.MessageDTO sendMessage(Long id, Long roomId, String message);

    List<InquiryChatResponse.MessageDTO> findAllByRoomId(Long roomId);
    InquiryChatResponse.MessageDTO sendAdminReply(Long roomId, String message, Long id) throws IOException;
}
