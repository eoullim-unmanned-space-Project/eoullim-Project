package org.example.eoullimback.user_auth.auth;

import jakarta.validation.Valid;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;

public interface AuthService {
    void signup(AuthRequest.@Valid SignupRequest requestDTO);
    User login(AuthRequest.@Valid LoginRequest requestDTO);
}
