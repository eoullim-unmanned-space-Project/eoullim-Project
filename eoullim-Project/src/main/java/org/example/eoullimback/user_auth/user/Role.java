package org.example.eoullimback.user_auth.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<UserRole> userRoles =  new HashSet<>();

    public Role(RoleType name) {
        this.name = name;
    }
}
