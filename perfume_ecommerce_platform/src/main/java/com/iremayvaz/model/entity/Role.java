package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data

/**
 * Bir ROLÜN,
 * ID'Sİ,
 * İSMİ,
 * SAHİPLERİ ?
 * olur.
 * */

// Role bilgileri
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<User> users;
}
