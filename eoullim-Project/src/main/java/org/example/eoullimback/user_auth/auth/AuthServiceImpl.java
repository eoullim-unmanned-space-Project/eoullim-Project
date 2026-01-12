package org.example.eoullimback.user_auth.auth;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    @Transactional
    public User signup(AuthRequest.SignupRequestDTO request) {

        request.validate();

        String hashPwd = passwordEncoder.encode(request.getPassword());

        User user = request.toEntity();
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
}
