package com.iremayvaz.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ProductResponse {
    @NotBlank
    String productName;

    @NotBlank
    String brandName;

    @Positive
    BigDecimal price;

    @Size(min = 0, max = 5)
    Double rating;
}
