package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data

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

    @Column(name = "product_name")
    private String productName;

    @Column(name = "brand_name")
    private String brandName;

    @OneToOne
    private Category category;

    private BigDecimal price;
    private Double rating;

    @Column(name = "top_notes")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_top_notes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> topNotes = new HashSet<>();

    @Column(name = "heart_notes")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_heart_notes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> heartNotes = new HashSet<>();

    @Column(name = "base_notes")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_base_notes",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> baseNotes = new HashSet<>();
}
