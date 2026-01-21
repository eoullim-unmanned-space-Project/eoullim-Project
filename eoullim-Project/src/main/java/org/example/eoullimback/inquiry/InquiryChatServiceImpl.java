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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryChatServiceImpl implements InquiryChatService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;
    private final InquiryChatRoomRepository inquiryChatRoomRepository;
    private final InquiryChatRoomService inquiryChatRoomService;

    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public SseEmitter connectInquiry(Long id) {

        Long roomId = inquiryChatRoomService.getOrCreateRoom(id);

        SseEmitter emitter = new SseEmitter(60L * 60 * 1000);

        emitterMap.put(roomId, emitter);

        emitter.onCompletion(() -> emitterMap.remove(roomId));
        emitter.onTimeout(() -> emitterMap.remove(roomId));
        emitter.onError(e -> emitterMap.remove(roomId));

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

        return new InquiryChatResponse.MessageDTO(inquiryChat);
    }

    @Override
    public List<InquiryChatResponse.MessageDTO> findAllByRoomId(Long roomId) {
        List<InquiryChat> chatEntities = inquiryRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId);

        return chatEntities.stream()
                .map(InquiryChatResponse.MessageDTO::new)
                .toList();
    }

}


