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
 * Bir SEPETİN,
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

    @OneToOne(optional = false, // bu ilişkiyi tutan alanın boş (null) olamaz.
            mappedBy = "cart")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Product> products;  // Bir sepette birden fazla ürün olabilir.
}