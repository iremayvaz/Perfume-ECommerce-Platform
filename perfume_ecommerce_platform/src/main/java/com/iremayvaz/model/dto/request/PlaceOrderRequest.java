package com.iremayvaz.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Sipariş oluşturulurken
 * sepet onaylanır,
 * adres bilgileri girilir,
 * ödeme bilgileri girilir,
 * ÖDEME BAŞARILI -> SİPARİŞ OLUŞTURULUR
 * ÖDEME BAŞARISIZ -> SİPARİŞ OLUŞTURULAMAZ, SEPET GERİ YÜKLENİR.
 * */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
// Sipariş oluşturma isteği
public class PlaceOrderRequest {
    private String shippingCity;
    private String shippingStreet;
    private String shippingDetail;
}
