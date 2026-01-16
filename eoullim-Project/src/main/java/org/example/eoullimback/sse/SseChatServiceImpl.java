package org.example.eoullimback.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseChatServiceImpl implements SseChatService {

    public final SseChatRepository sseChatRepository;

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter createConnection(String clientId) {

        SseEmitter emitter = new SseEmitter(60 * 1000L);

        emitterMap.put(clientId, emitter);
        log.info("SSE 연결 추가 : {}", clientId);

        emitter.onCompletion(() -> emitterMap.remove(clientId));
        emitter.onTimeout(() -> emitterMap.remove(clientId));
        emitter.onError((e) -> emitterMap.remove(clientId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결되었습니다."));
        } catch (IOException e) {
            log.error("초기 전송 실패: ", e);
        }


        return emitter;
    }

    @Override
    public void addMessage(String message, String sender) {

        SseChat sseChat = SseChat.builder()
                .message(message)
                .sender(sender)
                .build();
        sseChatRepository.save(sseChat);

        broadcast(message,  sender);
    }

    public void broadcast(String message, String sender) {

        String formattedMessage = sender + ":" + message;

        emitterMap.forEach((id, emitter) -> {

            try {
                emitter.send(SseEmitter
                        .event()
                        .name("newMessage")
                        .data(formattedMessage));
            } catch (IOException e) {
                emitterMap.remove(id);
            }
        });
    }

    public List<SseChat> findAll() {

        return sseChatRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
