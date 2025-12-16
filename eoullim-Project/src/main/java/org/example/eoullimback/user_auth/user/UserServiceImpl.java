package org.example.eoullimback.user_auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Object getMyProfile(Long userId) {
        return null;
    }

    @Override
    public void updateMyProfile(Long userId, UserRequest.UpdateProfileRequest request) {

    }

    @Override
    public void deleteMyProfile(Long userId) {

    }

    @Override
    public Object getAllUsers() {
        return null;
    }

    @Override
    public Object getUserById(Long id) {
        return null;
    }

    @Override
    public void updateUserRole(Long id, UserRequest.UpdateRoleRequest request) {

    }

    @Override
    public void deleteUser(Long id) {

    }
}
