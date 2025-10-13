package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
@Data

/**
 * Bir sepetin,
 * ID'Sİ,
 * SAHİBİ,
 * ÜRÜNLERİ
 * olur.
 * */

// Kullanıcı sepeti
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(cascade = CascadeType.ALL,
                orphanRemoval = true,
                mappedBy = "cart")
    private Set<Product> products;  // Bir sepette birden fazla ürün olabilir.
}