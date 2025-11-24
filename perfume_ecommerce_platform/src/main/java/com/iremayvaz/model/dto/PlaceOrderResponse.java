package com.iremayvaz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
