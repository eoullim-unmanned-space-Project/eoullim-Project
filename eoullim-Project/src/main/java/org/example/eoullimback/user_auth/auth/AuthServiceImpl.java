package org.example.eoullimback.user_auth.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MailService mailService;

    @Override
    @Transactional
    public User signup(AuthRequest.SignupRequestDTO request) {

        request.validate();

        Role userRole = roleRepository.findById(RoleType.USER)
                .orElseThrow(() -> new Exception403(ErrorCode.ROLE_NOT_FOUND));

        String hashPwd = passwordEncoder.encode(request.getPassword());

        User user = request.toEntity();
        user.addRole(userRole);
        user.setPassword(hashPwd);

        return authRepository.save(user);
    }

    @Override
    public User login(AuthRequest.LoginRequestDTO request) {

        request.validate();

        User userEntity = authRepository.findByLoginIdWithRoles(request.getLoginId())
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (userEntity.getStatus() == Status.WITHDRAWN) {
            throw new Exception400(ErrorCode.USER_STATUS_WITHDRAWN);
        }

        if (!passwordEncoder.matches(request.getPassword(),
                userEntity.getPassword()
        )) {
            throw new Exception401(ErrorCode.INVALID_PASSWORD);
        }

        return userEntity;
    }

    @Override
    public void verifyPassword(User sessionUser, String password) {

        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception401(ErrorCode.INVALID_PASSWORD);
        }

    }

    @Override
    @Transactional
    public void requestPasswordReset(AuthRequest.ResetPasswordRequestDTO request, HttpSession session) {

        request.validate();

        userRepository.findByLoginIdAndEmail(request.getLoginId(), request.getEmail())
                .ifPresent(user -> {

                    String token = UUID.randomUUID().toString();
                    session.setAttribute("PWD_RESET_TOKEN" + token, user.getId());

                    String resetLink =
                            "http://localhost:8080/reset-password?token=" + token;

                    mailService.sendPasswordResetLink(
                            user.getEmail(),
                            resetLink
                    );
                });
    }

    @Override
    @Transactional
    public void resetPassword(AuthRequest.ResetPasswordConfirmDTO request, HttpSession session) {

        request.validate();

        Long userId = (Long) session.getAttribute("PWD_RESET_TOKEN:" + request.getToken());

        if (userId == null) {
            throw new Exception400(ErrorCode.INVALID_INPUT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        session.removeAttribute("PWD_RESET_TOKEN:" + request.getToken());
    }
}
