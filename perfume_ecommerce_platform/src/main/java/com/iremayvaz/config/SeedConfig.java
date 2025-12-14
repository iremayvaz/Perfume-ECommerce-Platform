package com.iremayvaz.config;

import com.iremayvaz.model.entity.Role;
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.enums.RoleName;
import com.iremayvaz.repository.RoleRepository;
import com.iremayvaz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SeedConfig {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.email:admin@admin.com}")
    private String adminEmail;

    @Value("${app.seed.admin.phone-number:05511111111}")
    private String adminPhoneNumber;

    @Value("${app.seed.admin.password:Admin123!}")
    private String adminPassword;

    @Value("${app.seed.admin.first-name:Admin}")
    private String adminFirstName;

    @Value("${app.seed.admin.last-name:User}")
    private String adminLastName;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {

            createRoleIfNotExists(RoleName.USER);
            createRoleIfNotExists(RoleName.ADMIN);
            createAdminIfNotExists(adminEmail,
                                   adminPhoneNumber,
                                   adminPassword,
                                   adminFirstName,
                                   adminLastName);
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

    private void createAdminIfNotExists(String adminEmail,
                                        String adminPhoneNumber,
                                        String adminPassword,
                                        String adminFirstName,
                                        String adminLastName) {
        userRepository.findByEmail(adminEmail)
                .orElseGet(() -> {
                    if (adminEmail == null || adminEmail.isBlank() ||
                            adminPassword == null || adminPassword.isBlank()) {
                        throw new IllegalStateException(
                                "Admin seed açık ama app.seed.admin.email/password boş. application.properties kontrol et."
                        );
                    }

                    Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                            .orElseThrow(() -> new IllegalStateException("ADMIN rolü yok. Önce rol seed çalışmalı."));

                    User admin = new User();
                    admin.setEmail(adminEmail);
                    admin.setPhoneNumber(adminPhoneNumber);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setFirstName(adminFirstName);
                    admin.setLastName(adminLastName);

                    admin.setRoles(Set.of(adminRole));

                    return userRepository.save(admin);
                });
    }
}
