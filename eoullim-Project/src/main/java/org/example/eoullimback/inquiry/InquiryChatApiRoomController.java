package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryChatApiRoomController {

    private final InquiryChatRoomService inquiryChatRoomService;

    @PostMapping("/api/inquiry-rooms")
    public ResponseEntity<Long> getRoomId(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long roomId = inquiryChatRoomService.createOrGetRoom(userDetails.getUser().getId());

        return ResponseEntity.ok(roomId);
    }
}
