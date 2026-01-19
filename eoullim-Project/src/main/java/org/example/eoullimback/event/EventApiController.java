package org.example.eoullimback.event;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;

    @GetMapping("/api/user/events/today")
    ResponseEntity<EventResponse.DetailDTO> getDetailEvent (HttpSession session) {
        User user = (User) session.getAttribute("sessionUser");


        EventResponse.DetailDTO event = eventService.getDetailEvent(user.getId());

        return ResponseEntity.ok().body(event);
    }

    @PostMapping("/api/user/events")
    ResponseEntity<EventResponse.DetailDTO> createEvent(HttpSession session) {
        User user = (User) session.getAttribute("sessionUser");

        EventResponse.DetailDTO event = eventService.createEvent(user.getId());

        return ResponseEntity.ok().body(event);
    }

}
