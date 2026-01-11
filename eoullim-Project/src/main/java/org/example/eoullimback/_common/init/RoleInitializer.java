//package org.example.eoullimback._common.init;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.example.eoullimback._common.enums.user.Role;
//import org.example.eoullimback.user_auth.user.RoleRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class RoleInitializer {
//
//    private final RoleRepository roleRepository;
//
//    @PostConstruct
//    public void init() {
//        for (Role role : Role.values()) {
//            roleRepository.findById(role)
//                    .orElseGet(() -> roleRepository.save(new org.example.eoullimback.user_auth.user.Role(role)));
//        }
//    }
//}
