package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * Bir kullanıcının,
 * ID'Sİ,
 * ADRESLERİ,
 * İSMİ
 * SOYİSMİ,
 * ŞİFRESİ,
 * MAİLİ,
 * TELEFON NUMARASI,
 * ROLÜ,
 * olur.
 * */

// Kullanıcı Bilgileri
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "bcrypted_password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)              // Rol bilgisi LOGIN'de çekilsin. Sonra gereksiz
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();       // Bir kullanıcı hem USER hem ADMIN olabilir.

    @OneToMany(fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();  // Adresler : Bir kullanıcının birden fazla adresi olabilir.
}
