package org.example.eoullimback.user_auth.auth;

import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;

public interface AuthService {
    User signup(AuthRequest.SignupRequestDTO request);
    User login(AuthRequest.LoginRequestDTO request);
    boolean verifyPassword(Long id, String password);
}
