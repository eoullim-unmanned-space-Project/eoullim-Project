package org.example.eoullimback.admin.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.inquiry.InquiryChatService;
import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminInquiryChatApiController {

    private final InquiryChatService inquiryChatService;

    @GetMapping("/api/admin/inquiry-rooms/{roomId}/messages")
    ResponseEntity<List<InquiryChatResponse.MessageDTO>> getChatMessages(@PathVariable Long roomId) {

        List<InquiryChatResponse.MessageDTO> chatEntities = inquiryChatService.findAllByRoomId(roomId);

        return ResponseEntity.ok(chatEntities);

    }

}
