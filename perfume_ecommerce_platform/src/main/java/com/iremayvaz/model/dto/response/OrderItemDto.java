package com.iremayvaz.model.dto.response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderItemDto { // Sipariş detayındaki ürünler
    private String productName;
    private Integer quantity;
    private BigDecimal lineTotal;
    private String currency = "TRY";
}
