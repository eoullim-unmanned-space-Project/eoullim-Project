package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.inquiry.SenderType;
import org.example.eoullimback._common.enums.user.Role;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.example.eoullimback.user_auth.user.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryChatServiceImpl implements InquiryChatService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;
    private final InquiryChatRoomRepository inquiryChatRoomRepository;
    private final InquiryChatRoomService inquiryChatRoomService;

    private final Map<Long, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connectInquiry(Long id) {

        Long roomId = inquiryChatRoomService.getOrCreateRoom(id);

        System.out.println(roomId);

        SseEmitter emitter = new SseEmitter(60L * 60 * 1000);

       emitterMap.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitterFromList(roomId, emitter));
        emitter.onTimeout(() -> removeEmitterFromList(roomId, emitter));
        emitter.onError(e -> removeEmitterFromList(roomId, emitter));

        sendConnectEvent(emitter, roomId);

        return emitter;
    }

    private void sendConnectEvent(SseEmitter emitter, Long roomId) {
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data(roomId));
        } catch (IOException e) {
            log.error("SSE 초기 이벤트 실패", e);
        }
    }

    @Override
    @Transactional
    public InquiryChatResponse.MessageDTO sendMessage(Long id, Long roomId, String message) {

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        InquiryChatRoom inquiryChatRoomEntity = inquiryChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.INGUIRY_CHAT_ROOM_NOT_FOUND));

        if (!inquiryChatRoomEntity.isOwer(userEntity.getId())) {
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        List<Role> roles = userEntity.getRoles().stream()
                .map(UserRole::getRole)
                .toList();

        boolean isAdmin = roles.stream()
                .anyMatch(role -> role == Role.ADMIN);

        SenderType senderType = isAdmin
                ? SenderType.ADMIN
                : SenderType.USER;

        InquiryChat inquiryChat = InquiryChat.builder()
                .room(inquiryChatRoomEntity)
                .message(message)
                .sender(userEntity.getName())
                .senderType(senderType)
                .build();

        inquiryRepository.save(inquiryChat);
        InquiryChatResponse.MessageDTO response = new InquiryChatResponse.MessageDTO(inquiryChat);

        try {
            broadcastMessage(roomId, response);
        } catch (IOException e) {
            log.error("브로드 캐스트 실패", e);
        }

        return  response ;
    }

    @Override
    public List<InquiryChatResponse.MessageDTO> findAllByRoomId(Long roomId) {
        List<InquiryChat> chatEntities = inquiryRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId);

        return chatEntities.stream()
                .map(InquiryChatResponse.MessageDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public InquiryChatResponse.MessageDTO sendAdminReply(Long roomId, String message, Long id) throws IOException {

        User adminEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        InquiryChatRoom inquiryChatRoomEntity = inquiryChatRoomRepository.findByIdWithUser(roomId)
                .orElseThrow(() -> new Exception404(ErrorCode.INGUIRY_CHAT_ROOM_NOT_FOUND));

        inquiryChatRoomEntity.setAdminId(adminEntity);

        String userName = inquiryChatRoomEntity.getUser().getName();

        InquiryChat inquiryChat = InquiryChat.builder()
                .room(inquiryChatRoomEntity)
                .sender(adminEntity.getName())
                .message(message)
                .senderType(SenderType.ADMIN)
                .build();

        inquiryRepository.saveAndFlush(inquiryChat);

        InquiryChatResponse.MessageDTO response = new InquiryChatResponse.MessageDTO(inquiryChat);

        broadcastMessage(roomId, response);

        return response;
    }

    private void removeEmitterFromList(Long roomId, SseEmitter emitter) {
        List<SseEmitter> emitters = emitterMap.get(roomId);

        if (emitters != null) {
            emitters.remove(emitter);
        }

        if (emitters.isEmpty()) {
            emitterMap.remove(roomId);
        }
    }

    private void broadcastMessage(Long roomId, InquiryChatResponse.MessageDTO messageDTO) throws IOException{
        List<SseEmitter> emitters = emitterMap.get(roomId);

        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("chat")
                            .data(messageDTO)
                    );
                } catch (IOException e) {
                    removeEmitterFromList(roomId, emitter);
                }
            }
        }
    }
}


