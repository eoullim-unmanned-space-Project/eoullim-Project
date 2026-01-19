package org.example.eoullimback.event;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;

    @GetMapping("/api/user/events/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    ResponseEntity<EventResponse.DetailDTO> getDetailEvent (
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        User user = userDetails.getUser();

        EventResponse.DetailDTO event = eventService.getDetailEvent(user.getId());

        return ResponseEntity.ok().body(event);
    }

    @PostMapping("/api/user/events")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    ResponseEntity<EventResponse.DetailDTO> createEvent(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        EventResponse.DetailDTO event = eventService.createEvent(user.getId());

        return ResponseEntity.ok().body(event);
    }

}
