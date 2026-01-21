package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.inquiry.dto.request.InquiryChatRequest;
import org.example.eoullimback.inquiry.dto.response.InquiryChatResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InquiryChatApiController {

    private final InquiryChatService inquiryChatService;

    @PostMapping("/api/inquiry-rooms/{roomId}")
    ResponseEntity<InquiryChatResponse.MessageDTO> sendMessage(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody InquiryChatRequest.SendDTO request
    ) {
        User user = userDetails.getUser();

        request.validate();

        InquiryChatResponse.MessageDTO messageDTO = inquiryChatService.sendMessage(user.getId(), roomId, request.getMessage());

        return ResponseEntity.ok().body(messageDTO);
    }
}
