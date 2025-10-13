package com.iremayvaz.model.entity;

import com.iremayvaz.model.enums.NoteType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notes")
@AllArgsConstructor
@NoArgsConstructor
@Data

/**
 * Bir notanın,
 * ID'Sİ,
 * NOTA İSMİ,
 * NOTA TÜRÜ
 * olur.
 * */

// Parfüm notaları (Üst nota, kalp nota, alt nota)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_name", nullable = false)
    private String noteName;

    @Enumerated(EnumType.STRING)
    @Column(name = "note_type", nullable = false)
    private NoteType noteType;
}
