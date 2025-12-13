package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "roles",
        uniqueConstraints = @UniqueConstraint(name = "uk_roles_name", columnNames = "role_name"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

/**
 * Bir rolün,
 * ID'Sİ,
 * İSMİ,
 * olur.
 * */

// Role bilgileri
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleName name;

    @Override
    public String getAuthority() {
        // ROLE_ADMIN, ROLE_USER formatında döndürüyoruz
        return "ROLE_" + name.name();
    }
}
