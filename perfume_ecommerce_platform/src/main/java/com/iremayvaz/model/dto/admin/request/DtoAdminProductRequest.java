package com.iremayvaz.model.dto.admin.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoAdminProductRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String imageUrl;
    private String gender;
    private String concentration;
    private String season;
    private String accord;
    private BigDecimal price;
    private Integer stockQuantity;
    private Long categoryId; // Seçilen kategorinin ID'si
    private Set<Long> topNoteIds; // Seçilen üst notaların ID'leri
    private Set<Long> heartNoteIds;
    private Set<Long> baseNoteIds;
}


