package org.example.eoullimback.user_auth.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles r " +
            "WHERE u.loginId = :loginId AND u.password = :password")
    Optional<User> findByLoginIdAndPasswordWithRoles(@Param("loginId") String  loginId, @Param("password") String password);
    boolean existsByLoginId(@NotBlank(message = "아이디는 필수입니다.") @Size(min = 6, max = 20, message = "아이디는 6자이상 20자 이하여야 합니다.") String s);
    boolean existsByPassword(@NotBlank(message = "비밀번호는 필수입니다.") @Size(min = 8, max = 20, message = "비밀번호는 8자이상 20자 이하여야 합니다.") String password);
    boolean existsByEmail(@NotBlank(message = "이메일은 필수입니다.") @Email(message = "@ 를 포함하여야 합니다.") String email);
    boolean existsByPhone(@NotBlank(message = "휴대폰 값은 필수입니다.") String phone);
}

