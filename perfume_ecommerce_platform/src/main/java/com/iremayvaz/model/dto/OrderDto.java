package com.iremayvaz.model.dto;

import com.iremayvaz.model.entity.OrderItem;
import com.iremayvaz.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDto { // SİPARİŞ OLUŞTURULDU -> SİPARİŞ KODU DÖN
    private Set<OrderItem> orderItems = new HashSet<>();
    private User user;
    private LocalDateTime createdAt;
    private String shippingCity;
    private String shippingStreet;
    private String shippingDetail;
}
