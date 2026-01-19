package org.example.eoullimback.room;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    @PostMapping("/api/rooms/{roomId}")
    public ResponseEntity<?> DetailRoom(@PathVariable Long roomId, Model model) {

        RoomResponse.DetailDTO room = roomService.detailRoom(roomId);

        model.addAttribute("room", room);

        return ResponseEntity.ok().body(Map.of("room", room));
    }
}
