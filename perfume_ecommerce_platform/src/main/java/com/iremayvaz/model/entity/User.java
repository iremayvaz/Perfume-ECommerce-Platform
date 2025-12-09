package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")})
@Getter
@Setter
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

    @Column(nullable = false)
    @Email(message="Geçerli bir e-posta girin")
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "bcrypted_password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)              // Rol bilgisi LOGIN'de çekilsin. Sonra gereksiz
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();       // Bir kullanıcı hem USER hem ADMIN olabilir.

    @OneToMany(fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                mappedBy = "user")
    private Set<Address> addresses = new HashSet<>();  // Adresler : Bir kullanıcının birden fazla adresi olabilir.

    public void addAddress(Address address) {
        address.setUser(this);
        addresses.add(address);
    }
    public void removeAddress(Address address) {
        address.setUser(null);
        addresses.remove(address);
    }
}