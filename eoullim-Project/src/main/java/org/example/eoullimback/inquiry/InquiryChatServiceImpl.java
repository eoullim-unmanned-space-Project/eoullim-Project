package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InquiryChatServiceImpl implements InquiryChatService {

    private final InquiryRepository inquiryRepository;
    private final InquiryChatRoomRepository inquiryChatRoomRepository;

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter connect(String clientId, Long roomId) {
        String key = roomId + "_" + clientId; // room 단위 구독 키
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간
        emitterMap.put(key, emitter);

        emitter.onCompletion(() -> emitterMap.remove(key));
        emitter.onTimeout(() -> emitterMap.remove(key));
        emitter.onError((e) -> emitterMap.remove(key));

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결되었습니다."));
        } catch (IOException e) {
            emitter.complete();
        }

        return emitter;
    }

    public void sendMessage(InquiryChatRoom room, String sender, String receiver, String message) {

        InquiryChat chat = InquiryChat.builder()
                .room(room)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();
        inquiryRepository.save(chat);

        emitterMap.forEach((key, emitter) -> {
            if (key.startsWith(room.getId() + "_")) {
                try {
                    emitter.send(SseEmitter.event().name("newMessage")
                            .data(sender + ": " + message));
                } catch (IOException e) {
                    emitter.complete();
                    emitterMap.remove(key);
                }
            }
        });
    }

    public List<InquiryChat> getInquiryChats(InquiryChatRoom room) {
        return inquiryRepository.findAllByRoomOrderByCreatedAtAsc(room);
    }

    public InquiryChatRoom getOrCreateRoom(String userId, String adminId) {
        return inquiryChatRoomRepository.findByUserIdAndAdminId(userId, adminId)
                .orElseGet(() -> inquiryChatRoomRepository.save(
                        InquiryChatRoom.builder()
                                .userId(userId)
                                .adminId(adminId)
                                .build()
                ));
    }
}
