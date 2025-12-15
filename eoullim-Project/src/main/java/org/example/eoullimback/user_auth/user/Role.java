package org.example.eoullimback.user_auth.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }
}
