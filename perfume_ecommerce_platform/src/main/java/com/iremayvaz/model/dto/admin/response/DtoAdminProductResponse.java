package com.iremayvaz.model.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DtoAdminProductResponse {
    private Long id;
    private String name;
    private String brand;
    private Integer stockQuantity;
    private BigDecimal price;
}
