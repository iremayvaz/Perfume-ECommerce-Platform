package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.OrderState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

/**
 * Bir siparişin,
 * ID'Sİ,
 * ADRESİ,
 * SAHİBİ
 * ÜRÜNLERİ
 * olur.
 * */

// Kullanıcı siparişleri
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name =  "code", nullable = false,  unique = true, length = 32)
    private String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(nullable = false)
    @Positive
    private BigDecimal totalPrice;

    @OneToMany(cascade = CascadeType.ALL,
                orphanRemoval = true,
                mappedBy = "order")
    private Set<OrderItem> items = new HashSet<>();  // Ürünler

                                        // FetchType.LAZY : Uygulamanızın veriyi gerçekten kullanmaya karar verene kadar yüklemeyi erteleyeceği anlamına gelir.
    @ManyToOne(optional = false,        // bu ilişkiyi tutan alanın boş (null) olamaz.
                fetch = FetchType.LAZY) // ilgili verilerin ne zaman veritabanından yükleneceğini belirler.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at",  nullable = false)
    private LocalDateTime createdAt;

    // sipariş oluşturulduktan sonra adres sabitlenmeli.
    // Kullanıcı adresi güncellese bile sipariş adresi değişmemeli.
    @Column(nullable = false)
    private String shippingCity;

    @Column(nullable = false)
    private String shippingStreet;

    @Column(nullable = false)
    private String shippingDetail;

    public void addItem(OrderItem item){
        item.setOrder(this);
        items.add(item);
    }
    public void removeItem(OrderItem item){
        item.setOrder(null);
        items.remove(item);
    }
}
