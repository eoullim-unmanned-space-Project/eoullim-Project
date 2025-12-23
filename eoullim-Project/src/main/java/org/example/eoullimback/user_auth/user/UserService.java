package org.example.eoullimback.user_auth.user;

import jakarta.validation.Valid;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;

public interface UserService {
    User getMyProfile(Long id);
    void updateProfile(Long id, UserRequest.@Valid UpDateDTO update);
    void leaveUser(Long id);
    void deleteProfileImage(Long id);

    boolean existsByLoginId(String loginId);
}
