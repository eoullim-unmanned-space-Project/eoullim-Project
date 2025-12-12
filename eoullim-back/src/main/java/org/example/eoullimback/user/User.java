package org.example.eoullimback.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.eoullimback.user.enums.RoleType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Table(name = "users")
@NoArgsConstructor
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @CreationTimestamp
    private Timestamp createAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public User(Long id, String loginId, String password,
                String name, String phone, Timestamp createAt, Timestamp updatedAt
    ) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
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
