package com.iremayvaz.model.enums;

// SİPARİŞ DURUMLARI
public enum OrderState {
    PENDING_PAYMENT, // ödeme bekliyor
    CREATED, // oluşturuldu
    DELIVERED, // teslim edildi
    PAID, // ödendi
    CANCELED, // iptal edildi (user tarafından)
    PREPARED, // hazırlandı
    CANCELED_PAYMENT, // ödeme reddedildi (ör. yetersiz bakiye, int alışverişe kapalı vs.)
    PREPARING // hazırlanıyor
}
