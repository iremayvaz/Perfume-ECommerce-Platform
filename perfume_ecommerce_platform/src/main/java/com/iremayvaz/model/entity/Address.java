package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
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

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType type;   // Ev, İş, Okul vb.

    private String city;        // Şehir
    private String street;      // Mahalle
    private String detail;      // Detay

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",
                nullable = false) // Sahipsiz adres olamaz
    private User user;
}
