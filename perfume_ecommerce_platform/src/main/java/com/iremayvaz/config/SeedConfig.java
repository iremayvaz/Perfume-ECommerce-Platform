package com.iremayvaz.config;

import com.iremayvaz.model.entity.Role;
import com.iremayvaz.model.enums.RoleName;
import com.iremayvaz.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SeedConfig {

    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner seedRoles() {
        return args -> {
            createRoleIfNotExists(RoleName.USER);
            createRoleIfNotExists(RoleName.ADMIN);
        };
    }

    private void createRoleIfNotExists(RoleName roleName) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }
}
