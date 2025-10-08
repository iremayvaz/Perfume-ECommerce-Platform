package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor

// Kullanıcı adresleri
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber; // Adres sahibinin telefon numarası
    private String type;        // Ev, İş, Okul vb.
    private String city;        // Şehir
    private String street;      // Mahalle
    private String detail;      // Detay
}
