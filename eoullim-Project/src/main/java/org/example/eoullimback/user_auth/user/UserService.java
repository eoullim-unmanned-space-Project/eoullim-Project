package org.example.eoullimback.user_auth.user;

import jakarta.validation.Valid;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;

public interface UserService {
    User getMyProfile(Long userId);
    void updateProfile(Long userId, UserRequest.@Valid UpDateDTO upDateDTO);
    void leaveUser(Long userId);
    void deleteProfileImage(Long userId);
}
