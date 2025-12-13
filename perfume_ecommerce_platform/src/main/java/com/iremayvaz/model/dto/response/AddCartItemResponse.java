package com.iremayvaz.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AddCartItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;

    private BigDecimal unitPriceSnapshot;
    private BigDecimal subTotal;

    private LocalDateTime priceLockedUntil;
    private String currency;
}
