package com.iremayvaz.model.dto.response;

import com.iremayvaz.model.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Sipariş oluşturduktan sonra
 * sistem sipariş kodu verir
 * */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
// Sipariş oluşturduktan sonra sistem cevabı
public class PlaceOrderResponse {
    private String orderCode;
    private OrderState orderState;
    private BigDecimal totalPrice;
}
