package org.example.eoullimback.user_auth.user;

import org.example.eoullimback.user_auth.user.dto.request.UserRequest;

public interface UserService {
    User getMyProfile(Long id);

    User updateProfile(Long id, UserRequest.UpDateDTO update);

    void leaveUser(Long id);

    void deleteProfileImage(Long id);

    boolean existsByLoginId(String loginId);
    User kakaoSocialLogin(String code);

    void verifyPassword(Long id, String password);

    User findByEmail(String email);

    String findLoginIdByNameAndEmail(String name, String email);

    User findByUserIdAndEmail(String userId, String email);

    void updatePassword(String userId, String newPassword);
}
