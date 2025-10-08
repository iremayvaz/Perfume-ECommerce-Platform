package com.iremayvaz.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

// Kullanıcı Bilgileri
public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Order order;        // Siparişler
    private Cart cart;          // Sepet
    private Address address;    // Adresler
}
