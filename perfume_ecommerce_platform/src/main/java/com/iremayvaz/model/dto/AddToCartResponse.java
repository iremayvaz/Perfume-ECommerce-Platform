package com.iremayvaz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AddToCartResponse {
    private Long id;
    private List<AddCartItemResponse> cartItems;
    private BigDecimal total;
    private boolean pricesRefreshed;
}
