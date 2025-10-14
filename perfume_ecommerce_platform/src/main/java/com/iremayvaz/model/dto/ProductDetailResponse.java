package com.iremayvaz.model.dto;

import com.iremayvaz.model.entity.Category;
import com.iremayvaz.model.entity.Note;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ProductDetailResponse {
    private Long id;
    private String productName;
    private String brandName;
    private Category category;
    private BigDecimal price;
    private Double rating;
    private Set<Note> topNotes;
    private Set<Note> heartNotes;
    private Set<Note> baseNotes;
}
