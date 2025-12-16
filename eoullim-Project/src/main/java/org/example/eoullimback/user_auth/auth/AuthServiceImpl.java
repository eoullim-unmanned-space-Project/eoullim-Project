package org.example.eoullimback.user_auth.auth;

import org.example.eoullimback.user_auth.auth.dto.AuthRequest;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public void signup(AuthRequest.SignupRequest request) {

    }

    @Override
    public User login(AuthRequest.LoginRequest request) {
        return null;
    }

    @Override
    public void logout(Long userId) {

    }
}
