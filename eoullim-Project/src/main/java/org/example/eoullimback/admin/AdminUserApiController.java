package org.example.eoullimback.admin;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserApiController {

    private final UserService userService;

    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable Long userId,
                                        @RequestBody AuthRequest.UserSuspendRequestDTO request
    ) {
        request.validate();
        userService.suspendUser(userId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/restore")
    public ResponseEntity<Void> restoreUser(@PathVariable Long userId) {
        userService.restoreUser(userId);
        return ResponseEntity.ok().build();
    }
}
