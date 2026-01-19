package org.example.eoullimback.admin.room;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.room.Room;
import org.example.eoullimback.room.RoomRequest;
import org.example.eoullimback.room.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiRoomController {

    private final RoomService roomService;

    @PostMapping("/api/admin/rooms")
    public ResponseEntity<Room> createRoom(Model model,
                                           RoomRequest.CreateDTO createDTO
    ) {
        createDTO.validate();

        Room room = roomService.createRoom(createDTO);
        model.addAttribute("room", room);

        return ResponseEntity.ok().body(room);
    }

    @PutMapping("/api/admin/rooms/{roomId}")
    public ResponseEntity<Map<String, Room>> updateRoom(@PathVariable Long roomId,
                                                        RoomRequest.UpdateDTO updateDTO
    ) {

        updateDTO.validate();

        Room room = roomService.updateRoom(roomId, updateDTO);

        return ResponseEntity.ok().body(Map.of("room", room));
    }


    @DeleteMapping("/api/admin/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {

        roomService.deleteRoom(roomId);

        return ResponseEntity.ok().body(null);
    }
}
