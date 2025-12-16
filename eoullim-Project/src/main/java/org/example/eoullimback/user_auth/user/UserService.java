package org.example.eoullimback.user_auth.user;

public interface UserService {

    Object getMyProfile(Long userId);

    void updateMyProfile(Long userId, UserRequest.UpdateProfileRequest request);

    void deleteMyProfile(Long userId);

    Object getAllUsers();

    Object getUserById(Long id);

    void updateUserRole(Long id, UserRequest.UpdateRoleRequest request);

    void deleteUser(Long id);
}
