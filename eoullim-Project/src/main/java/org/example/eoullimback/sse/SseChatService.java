package org.example.eoullimback.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface SseChatService{
    SseEmitter createConnection(String clientId);

    void addMessage(String message, String sender);

    List<SseChat> findAll();
}
