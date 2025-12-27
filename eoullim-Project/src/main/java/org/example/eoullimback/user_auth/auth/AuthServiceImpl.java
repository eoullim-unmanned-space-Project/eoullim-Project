package org.example.eoullimback.user_auth.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User signup(AuthRequest.@Valid SignupRequestDTO request) {

        if (authRepository.existsByLoginId(request.getLoginId())) {
            throw new Exception409(ErrorCode.USER_CONFLICT_ID);
        }

        if (authRepository.existsByEmail(request.getEmail())) {
            throw new Exception409(ErrorCode.USER_CONFLICT_EMAIL);
        }

        if (authRepository.existsByPhone(request.getPhone())) {
            throw new Exception409(ErrorCode.USER_CONFLICT_PHONE_NUMBER);
        }

        String hashPwd = passwordEncoder.encode(request.getPassword());

        User user = request.toEntity();
        user.setPassword(hashPwd);

        return authRepository.save(user);
    }

    @Override
    public User login(AuthRequest.@Valid LoginRequestDTO request) {

        User userEntity = authRepository.findByLoginIdAndPassword(request.getLoginId(), request.getPassword())
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (userEntity.getStatus() == Status.WITHDRAWN) {
            throw new Exception400(ErrorCode.USER_STATUS_WITHDRAWN);
        }

        return userEntity;
    }
}
