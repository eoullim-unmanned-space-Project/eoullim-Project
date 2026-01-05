package org.example.eoullimback.user_auth.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback._common.error.exception.Exception400;
import org.example.eoullimback._common.error.exception.Exception403;
import org.example.eoullimback._common.error.exception.Exception404;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.example.eoullimback.user_auth.user.Role;
import org.example.eoullimback.user_auth.user.RoleRepository;
import org.example.eoullimback.user_auth.user.User;
import org.example.eoullimback.user_auth.user.UserRepository;
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
    private final RoleRepository roleRepository;

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

        User userEntity = authRepository.findByLoginIdAndPasswordWithRoles(request.getLoginId(), request.getPassword())
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        if (userEntity.getStatus() == Status.WITHDRAWN) {
            throw new Exception400(ErrorCode.USER_STATUS_WITHDRAWN);
        }

        return userEntity;
    }
}
