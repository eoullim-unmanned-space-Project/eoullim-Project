package org.example.eoullimback.user_auth.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.eoullimback._common.base.BaseTimeEntity;
import org.example.eoullimback._common.enums.RoleType;
import org.example.eoullimback._common.enums.user.OAuthProvider;
import org.example.eoullimback._common.enums.user.Status;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_users_login_id", columnNames = "login_id"),
            @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        })
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseTimeEntity {
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

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    private LocalDateTime withdrawnAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'LOCAL'")
    private OAuthProvider provider;

    @Builder
    public User(Long id, String loginId, String password, String name,
                String phone, String  email, Status status, String profileImage,
                OAuthProvider provider
    ) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profileImage = profileImage;
        this.status = status != null ? status : Status.ACTIVE;
        this.provider = provider;

        // 방어적 코드 작성 : 만약 null 값이면 기본 LOCAL로 저장
        if (provider == null) {
            this.provider = OAuthProvider.LOCAL;
        } else {
            this.provider = provider;
        }
    }

    public void update(UserRequest.UpDateDTO upDateDTO) {
        this.name = upDateDTO.getName();
        this.email = upDateDTO.getEmail();

        if (upDateDTO.getUseProfileFileName() != null
                && !upDateDTO.getUseProfileFileName().trim().isEmpty()) {
            this.profileImage = upDateDTO.getUseProfileFileName();
        }
    }

    public void userWithdrawn(Status status) {
        this.status = status;
        this.withdrawnAt = LocalDateTime.now();
    }

    public void clearProfileImage() {
        this.profileImage = null;
    }

    public String getProfileImageUrl() {
        if (profileImage == null || profileImage.isEmpty()) {
            return "/images/default-profile.png";
        }
        return "/uploads/" + profileImage;
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
