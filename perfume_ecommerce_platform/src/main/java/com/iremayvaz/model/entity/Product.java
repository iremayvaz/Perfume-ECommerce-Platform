package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

/**
 * Bir ürünün,
 * ID'Sİ,
 * İSMİ,
 * MARKA ADI,
 * KATEGORİSİ,
 * FİYATI,
 * DEĞERLENDİRMESİ,
 * ÜST NOTASI,
 * KALP NOTASI,
 * ALT NOTASI
 * olur.
 * */

// Parfüm bilgileri
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    private String brand;

    @ManyToOne(fetch = FetchType.LAZY,
                optional = false)
    @JoinColumn(name = "category_id", nullable = false)             // karşı entity'den gelen FK
    private Category category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Min(0)
    @Max(5)
    private Double rating;

    @Column(name = "description", length = 1000) // Uzun metinler için length arttırılabilir
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_top_notes",
            joinColumns = @JoinColumn(name = "product_id"),         // bu entity'nin FK'sı
            inverseJoinColumns = @JoinColumn(name = "note_id"))     // karşı entity'nin FK'sı
    private Set<Note> topNotes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_heart_notes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> heartNotes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_base_notes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> baseNotes = new HashSet<>();
}
