package org.example.eoullimback.inquiry;

import org.example.eoullimback.inquiry.dto.response.InquiryChatRoomResponse;

import java.util.List;

public interface InquiryChatRoomService {
    Long getOrCreateRoom(Long id);

    List<InquiryChatRoomResponse.ListDTO> getAllInquiry();

    Long createOrGetRoom(Long userId);
}
