package org.example.eoullimback.user_auth.user;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Object getMyProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
    }

    @Override
    @Transactional
    public void updateMyProfile(Long userId, UserRequest.UpdateProfileRequest request) {
        User user = (User) getMyProfile(userId);
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
    }

    @Override
    @Transactional
    public void deleteMyProfile(Long userId) {
        userRepository.deleteById(userId);
    }

    // 관리자용
    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, UserRequest.UpdateRoleRequest request) {
        User user = getUserById(userId);
        Role role = roleRepository.findById(RoleType.valueOf(request.getRole()))
                .orElseThrow(() -> new IllegalArgumentException("해당 권한이 없습니다."));
        user.getUserRoles().clear();
        user.addRole(role);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
