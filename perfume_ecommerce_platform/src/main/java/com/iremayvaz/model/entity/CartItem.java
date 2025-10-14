package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(name = "uk_cart_product", // aynı ürün tekrar eklenirse yeni satır yerine mevcut satırın quantity’si artırılır.
                                            columnNames = {"cart_id","product_id"}),
        indexes = { // Sepeti ve ürünü hızlı bulmak için
                @Index(name = "ix_cart_items_cart", columnList = "cart_id"),
                @Index(name = "ix_cart_items_product", columnList = "product_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

/**
 * Bir kullanıcı sepetindeki item'ın,
 * ID'Sİ,
 * SEPET BİLGİSİ,
 * ÜRÜN BİLGİSİ,
 * MİKTARI,
 * BİRİM FİYAT BİLGİSİ
 * olur.
 * */

// Kullanıcının sepetindeki ürün
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Positive
    private Integer quantity;

    // sepete eklendiği andaki birim fiyatı sabitle. fiyat değişse bile fatura sabit kalır.
    @Column(name = "unit_price_snapshot", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPriceSnapshot;

    // ödeme yapılmayan ürün sepetteyken fiyat kontrolü yapılır.
    @Column(name="price_locked_until")
    private LocalDateTime priceLockedUntil;

    @Column(name="currency", length=3)
    private String currency = "TRY";
}

