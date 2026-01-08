package org.example.eoullimback.user_auth.user;

import jakarta.servlet.http.HttpSession;
import org.example.eoullimback.user_auth.auth.dto.request.AuthRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);

    Optional<User> findByName(String name);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNameAndEmail(String name, String email);

    Optional<User> findByLoginIdAndEmail(String loginId, String email);
}
