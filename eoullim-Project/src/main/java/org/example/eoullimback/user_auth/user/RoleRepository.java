package org.example.eoullimback.user_auth.user;

import org.example.eoullimback._common.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleType> {
}
