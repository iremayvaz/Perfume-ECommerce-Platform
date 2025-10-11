package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * Bir ADRESİN,
 * ID'Sİ,
 * SAHİPLERİ,
 * TÜRÜ,
 * ŞEHRİ,
 * MAHALLESİ,
 * DETAYI
 * ÜRÜNLERİ
 * olur.
 * */

// Kullanıcı adresleri
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "user")
    private Set<User> user;     // Kullanıcılar : Bir adrese kayıtlı birden fazla kullanıcı olabilir

    @Column(name = "address_type")
    private String type;        // Ev, İş, Okul vb.

    private String city;        // Şehir
    private String street;      // Mahalle
    private String detail;      // Detay
}
