package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.Concentration;
import com.iremayvaz.model.enums.Gender;
import com.iremayvaz.model.enums.Season;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Data

// Parfüm kategori bilgileri
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Gender gender;      // Kokunun cinsiyeti
    private Concentration name; // EDT, EDP, PARFUME vb.
    private Season season;      // Yazlık, kışlık koku
}
