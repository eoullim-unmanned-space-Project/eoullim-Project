package org.example.eoullimback.event;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;

    @PostMapping("/api/events/today")
    ResponseEntity<EventResponse.DetailDTO> createFortune(HttpSession session) {
        User user = (User) session.getAttribute("sessionUser");

        EventResponse.DetailDTO event = eventService.createFortune(user.getId());

        return ResponseEntity.ok().body(event);
    }


}
