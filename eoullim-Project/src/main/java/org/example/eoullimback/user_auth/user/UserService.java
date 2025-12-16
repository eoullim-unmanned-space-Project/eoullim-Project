package org.example.eoullimback.user_auth.user;

public interface UserService {

    Object getMyProfile(Long userId);

    void updateMyProfile(Long userId, UserRequest.UpdateProfileRequest request);

    void deleteMyProfile(Long userId);

    Object getAllUsers();

    Object getUserById(Long userId);

    void updateUserRole(Long userId, UserRequest.UpdateRoleRequest request);

    void deleteUser(Long userId);
}
