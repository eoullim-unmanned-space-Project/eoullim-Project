package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;

public interface AuthService {
    User signup(AuthRequest.SignupRequestDTO request);
    User login(AuthRequest.LoginRequestDTO request);
    void verifyPassword(User sessionUser, String password);
    void requestPasswordReset(AuthRequest.ResetPasswordRequestDTO request, HttpSession session);
    void resetPassword(AuthRequest.ResetPasswordConfirmDTO request, HttpSession session);
}
