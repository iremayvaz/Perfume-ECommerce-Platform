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
@Table(name = "orderItem",
        uniqueConstraints = @UniqueConstraint(name = "uk_order_product", // aynı ürün tekrar eklenirse yeni satır yerine mevcut satırın quantity’si artırılır.
                columnNames = {"order_id","product_id"}),
        indexes = { // Sepeti ve ürünü hızlı bulmak için
                @Index(name = "ix_order_items_order", columnList = "order_id"),
                @Index(name = "ix_order_items_product", columnList = "product_id")
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

/**
 * Bir kullanıcı sepetindeki item'ın,
 * ID'Sİ,
 * SEPET BİLGİSİ,
 * ÜRÜN BİLGİSİ,
 * MİKTARI,
 * BİRİM FİYAT BİLGİSİ
 * olur.
 * */

// Kullanıcının siparişindeki item
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Positive
    private Integer quantity;

    // sipariş anındaki birim fiyat
    @Column(name = "unit_price_snapshot", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPriceSnapshot;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal; // quantity * unitPriceSnapshot

    @Column(length=3)
    private String currency = "TRY";
}
