package com.iremayvaz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notes")
@AllArgsConstructor
@NoArgsConstructor
@Data

// Parfüm notaları (Üst nota, kalp nota, alt nota)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
