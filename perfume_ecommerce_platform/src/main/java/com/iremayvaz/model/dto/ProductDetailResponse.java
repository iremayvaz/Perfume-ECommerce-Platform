package com.iremayvaz.model.dto;

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
    private CategoryDetailResponse category;
    private BigDecimal price;
    private Double rating;
    private Set<String> topNotes;
    private Set<String> heartNotes;
    private Set<String> baseNotes;
}
