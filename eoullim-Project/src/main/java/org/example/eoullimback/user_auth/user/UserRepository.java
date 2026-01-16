package org.example.eoullimback.user_auth.user;

import org.example.eoullimback._common.enums.user.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);

    Optional<User> findByName(String name);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNameAndEmail(String name, String email);

    Optional<User> findByLoginIdAndEmail(String loginId, String email);

    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByNameAndEmailAndStatus(String name, String email, Status status);

    Optional<User> findByLoginIdAndEmailAndStatus(String loginId, String email, Status status);

    boolean existsByEmailAndStatus(String email, Status status);

    long countByStatus(Status status);
    long countByStatusAndCreatedAtLessThanEqual(
            Status status,
            LocalDateTime createdAt
    );
}
