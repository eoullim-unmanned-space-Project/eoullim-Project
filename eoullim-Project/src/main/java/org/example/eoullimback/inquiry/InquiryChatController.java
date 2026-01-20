package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/inquiry/chat")
@RequiredArgsConstructor
public class InquiryChatController {

    private final InquiryChatServiceImpl chatService;

    /**
     * SSE 연결
     */
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestParam String clientId,
                              @RequestParam Long roomId
    ) {
        return chatService.connect(clientId, roomId);
    }

    /**
     * 메시지 전송
     */
    @PostMapping("/send")
    public void send(@RequestParam String sender,
                     @RequestParam String receiver,
                     @RequestParam String message,
                     @RequestParam String userId,
                     @RequestParam String adminId
    ) {
        InquiryChatRoom room = chatService.getOrCreateRoom(userId, adminId);

        chatService.sendMessage(room, sender, receiver, message);
    }

    /**
     * 채팅 기록 조회
     */
    @GetMapping("/history")
    public List<InquiryChat> history(@RequestParam String userId,
                                     @RequestParam String adminId
    ) {
        InquiryChatRoom room = chatService.getOrCreateRoom(userId, adminId);

        return chatService.getInquiryChats(room);
    }

    /**
     * 방 조회 / 생성
     */
    @GetMapping("/room")
    public InquiryChatRoom getRoom(@RequestParam String userId, @RequestParam String adminId) {
        return chatService.getOrCreateRoom(userId, adminId);
    }
}
