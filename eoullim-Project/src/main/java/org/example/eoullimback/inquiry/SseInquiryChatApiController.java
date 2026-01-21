package org.example.eoullimback.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseInquiryChatApiController {

    private final InquiryChatService inquiryChatService;

    @GetMapping(value = "/api/inquiry-rooms", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectInquiry(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        return inquiryChatService.connectInquiry(user.getId());
    }
}
