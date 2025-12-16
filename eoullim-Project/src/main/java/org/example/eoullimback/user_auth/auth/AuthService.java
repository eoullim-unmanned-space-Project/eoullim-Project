package org.example.eoullimback.user_auth.auth;

import org.example.eoullimback.user_auth.auth.dto.AuthRequest;
import org.example.eoullimback.user_auth.user.User;

public interface AuthService {
    void signup(AuthRequest.SignupRequest request);

    User login(AuthRequest.LoginRequest request);

    void logout(Long userId);
}
