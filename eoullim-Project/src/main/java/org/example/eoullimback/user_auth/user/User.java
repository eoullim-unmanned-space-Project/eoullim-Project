package org.example.eoullimback.user_auth.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Table(name = "users",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_users_login_id", columnNames = "login_id"),
            @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        })
@NoArgsConstructor
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 30)
    private String phone;

    @Column(nullable = false, length = 255)
    private String email;

    @CreationTimestamp
    private Timestamp createAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    public User(Long id, String loginId, String password, String name, String phone, String email
    ) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public void addRole(Role role) {
        boolean exists = userRoles.stream().anyMatch(u -> u.getRole().equals(role));

        if (!exists) {
            userRoles.add(new UserRole(this, role));
        }
    }

    public Set<RoleType> getRoleTypes() {
        return userRoles.stream()
                .map(u -> u.getRole().getName())
                .collect(Collectors.toUnmodifiableSet());
    }

    public void deleteRole(Role role) {
        userRoles.removeIf(u -> u.getRole().equals(role));
    }
}
