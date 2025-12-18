package com.iremayvaz.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ProductResponse {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stockQuantity;
    private Double rating;
    private String imageUrl;

    private String concentration; // Category entity'sinden gelecek (EDP, EDT vs.)
    private Set<String> topNotes; // İsim listesi olarak
    private Set<String> heartNotes;
    private Set<String> baseNotes;
}
