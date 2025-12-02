package com.iremayvaz.model.enums;

// SİPARİŞ DURUMLARI
public enum OrderState {
    CREATED, // oluşturuldu
    PREPARING, // hazırlanıyor
    PREPARED, // hazırlandı
    DELIVERED, // teslim edildi
    CANCELED, // iptal edildi (user tarafından)
    CANCELED_PAYMENT // ödeme reddedildi (ör. yetersiz bakiye, int alışverişe kapalı vs.)
}
