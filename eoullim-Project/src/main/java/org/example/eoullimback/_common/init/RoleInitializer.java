package org.example.eoullimback._common.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.RoleType;
import org.example.eoullimback.user_auth.user.Role;
import org.example.eoullimback.user_auth.user.RoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        for (RoleType roleType : RoleType.values()) {
            roleRepository.findById(roleType)
                    .orElseGet(() -> roleRepository.save(new Role(roleType)));
        }
    }
}
