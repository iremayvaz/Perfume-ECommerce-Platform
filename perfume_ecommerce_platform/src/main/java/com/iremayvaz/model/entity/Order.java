package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data

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

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "order")
    private Set<Product> products;  // Ürünler

    // FetchType.LAZY : Uygulamanızın veriyi gerçekten kullanmaya karar verene kadar yüklemeyi erteleyeceği anlamına gelir.
    @ManyToOne(optional = false,    // bu ilişkiyi tutan alanın boş (null) olamaz.
            fetch = FetchType.LAZY) // ilgili verilerin ne zaman veritabanından yükleneceğini belirler.
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
}
