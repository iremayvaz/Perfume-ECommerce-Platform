package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.Concentration;
import com.iremayvaz.model.enums.Gender;
import com.iremayvaz.model.enums.Season;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

/**
 * Bir kategori,
 * ID'Sİ,
 * CİNSİYETİ,
 * KONSANTRASYON TÜRÜ,
 * MEVSİMİ,
 * ETİKETİ
 * içerir.
 * */

// Parfüm kategori bilgileri
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;      // Kokunun cinsiyeti

    @Column(name = "concentration_name")
    @Enumerated(EnumType.STRING)
    private Concentration concentrationName; // EDT, EDP, PARFUME vb.

    @Column(name = "season")
    @Enumerated(EnumType.STRING)
    private Season season;      // Yazlık, kışlık koku

    @Column(name = "accord")
    private String accord;      // Etiket (Ör. woody, fresh, oriental vs.)
}
