package org.example.eoullimback.admin.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.inquiry.InquiryChatService;
import org.example.eoullimback.inquiry.dto.request.InquiryChatRequest;
import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        @PostMapping("/api/admin/inquiry-rooms/{roomId}/messages")
        ResponseEntity<?> sendAdminReply(
                @PathVariable Long roomId,
                @RequestBody InquiryChatRequest.SendDTO request,
                @AuthenticationPrincipal CustomUserDetails adminDetails
        ) {

            User admin = adminDetails.getUser();

            inquiryChatService.sendAdminReply(roomId, request.getMessage(),admin.getId());

            return ResponseEntity.ok().body("null");
        }


}
