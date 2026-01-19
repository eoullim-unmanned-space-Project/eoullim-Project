    package org.example.eoullimback.user_auth.user;

    import lombok.RequiredArgsConstructor;
    import org.example.eoullimback._common.enums.errors.ErrorCode;
    import org.example.eoullimback._common.error.exception.Exception404;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            User userEntity = userRepository.findByUsernameWithRoles(username)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

            return new CustomUserDetails(userEntity);
        }
    }
