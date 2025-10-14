package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

/**
 * Bir adresin,
 * ID'Sİ,
 * ADRES TÜRÜ,
 * ŞEHRİ,
 * MAHALLESİ,
 * DETAYI,
 * SAHİBİ
 * olur.
 * */

// Kullanıcı adresleri
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType type;   // Ev, İş, Okul vb.

    @Column(nullable = false)
    private String city;        // Şehir

    @Column(nullable = false)
    private String street;      // Mahalle

    @Column(nullable = false)
    private String detail;      // Detay

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",
                nullable = false) // Sahipsiz adres olamaz
    private User user;
}
