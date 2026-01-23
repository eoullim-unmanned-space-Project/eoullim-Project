package org.example.eoullimback.admin.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.inquiry.InquiryChatService;
import org.example.eoullimback.inquiry.dto.request.InquiryChatRequest;
import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminInquiryChatApiController {

    private final InquiryChatService inquiryChatService;

    @GetMapping(value = "/api/admin/inquiry-rooms/{roomId}/connect",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectToInquiryRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails adminDetails
    ) {
        User admin = adminDetails.getUser();
        return inquiryChatService.connectInquiry(roomId);
    }

    @GetMapping("/api/admin/inquiry-rooms/{roomId}/messages")
    ResponseEntity<List<InquiryChatResponse.MessageDTO>> getChatMessages(@PathVariable Long roomId) {

        List<InquiryChatResponse.MessageDTO> chatEntities = inquiryChatService.findAllByRoomId(roomId);

        return ResponseEntity.ok(chatEntities);

    }

    @PostMapping("/api/admin/inquiry-rooms/{roomId}/messages")
    ResponseEntity<InquiryChatResponse.MessageDTO> sendAdminReply(
            @PathVariable Long roomId,
            @RequestBody InquiryChatRequest.SendDTO request,
            @AuthenticationPrincipal CustomUserDetails adminDetails
    ) throws IOException {

        User admin = adminDetails.getUser();

         InquiryChatResponse.MessageDTO messageDTO = inquiryChatService.sendAdminReply(roomId, request.getMessage(),admin.getId());

        return ResponseEntity.ok().body(messageDTO);
    }


}
