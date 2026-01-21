package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.inquiry.SenderType;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.inquiry.dto.response.InquiryChatRoomResponse;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryChatRoomServiceImpl implements InquiryChatRoomService{

    private final InquiryChatRoomRepository inquiryChatRoomRepository;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public InquiryChatRoomResponse.CreateResponse createOrGetRoom(Long id) {

        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        InquiryChatRoom inquiryChatRoom = InquiryChatRoom.builder()
                .user(userEntity)
                .build();

        inquiryChatRoomRepository.save(inquiryChatRoom);

        return new InquiryChatRoomResponse.CreateResponse(inquiryChatRoom);
    }

    @Override
    @Transactional
    public Long getOrCreateRoom(Long userId) {

        User userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        InquiryChatRoom room = inquiryChatRoomRepository.findByUserId(userEntity.getId())
            .orElseGet(() ->
                inquiryChatRoomRepository.save(
                        InquiryChatRoom.builder()
                        .user(userEntity)
                        .build()
                )
            );

        return room.getId();
    }

    @Override
    public List<InquiryChatRoomResponse.ListDTO> getAllInquiry() {

        List<InquiryChatRoom> inquiryChatRoomEntities = inquiryChatRoomRepository.findAllWithUser();

        List<InquiryChatRoomResponse.ListDTO> dtoList = new ArrayList<>();

        for (InquiryChatRoom inquiryChatRoom : inquiryChatRoomEntities) {
            String lastMsg = inquiryRepository.findLatestChatByRoomId(inquiryChatRoom.getId())
                    .map(InquiryChat::getMessage)
                    .orElse("대화 내용이 없습니다.");

            dtoList.add(new InquiryChatRoomResponse.ListDTO(inquiryChatRoom, lastMsg));
        }
        return dtoList;
    }
}
