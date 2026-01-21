package org.example.eoullimback.inquiry;

import org.example.eoullimback.inquiry.dto.response.InquiryChatRoomResponse;

import java.util.List;

public interface InquiryChatRoomService {
    InquiryChatRoomResponse.CreateResponse createOrGetRoom(Long id);
    Long getOrCreateRoom(Long id);

    List<InquiryChatRoomResponse.ListDTO> getAllInquiry();
}
