package org.example.eoullimback.timeslot;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.MediaType;
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
    public SseEmitter connect(
            @PathVariable Long roomId,
            HttpSession session
    ) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        return sseTimeSlotService.createConnection(roomId, sessionUser.getId());
    }

}
