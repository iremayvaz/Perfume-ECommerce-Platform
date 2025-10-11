package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
 * SEPETİ,
 * SİPARİŞLERİ
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;             // Bir kullanıcının bir rolü olabilir.

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private Set<Order> orders;     // Siparişler : Bir kullanıcının birden fazla siparişi olabilir.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;             // Sepet : Bir kullanıcının bir sepeti olabilir.

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Address> address;  // Adresler : Bir kullanıcının birden fazla adresi olabilir.
}
