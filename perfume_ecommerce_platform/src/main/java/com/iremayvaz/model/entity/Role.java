package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;


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
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleName roleName;
}
