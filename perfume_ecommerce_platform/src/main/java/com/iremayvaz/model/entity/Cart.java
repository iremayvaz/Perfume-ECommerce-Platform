package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    // optimistic locking otomatik aktif
    // Hibernate her güncellemede versiyonu kontrol eder;
    // çakışma olursa hata atar → veri kaybı önlenir
    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    @OrderBy("id ASC")
    private List<CartItem> items = new ArrayList<>();  // Bir sepette birden fazla item olabilir.

    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
    }
    public void removeItem(CartItem item) {
        item.setCart(null);
        items.remove(item);
    }
}