package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();  // Bir sepette birden fazla item olabilir.

    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
    }
    public void removeItem(CartItem item) {
        item.setCart(null);
        items.remove(item);
    }
}