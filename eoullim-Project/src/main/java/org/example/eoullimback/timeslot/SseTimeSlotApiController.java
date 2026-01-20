package org.example.eoullimback.timeslot;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseTimeSlotApiController {

    private final SseTimeSlotService sseTimeSlotService;

    @GetMapping(value = "/api/sse/{roomId}/timeslot-connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public SseEmitter connect(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        User user = userDetails.getUser();

        return sseTimeSlotService.createConnection(roomId, user.getId());
    }

}
