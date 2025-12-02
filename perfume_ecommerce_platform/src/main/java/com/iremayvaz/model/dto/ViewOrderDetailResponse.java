package com.iremayvaz.model.dto;

import com.iremayvaz.model.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ViewOrderDetailResponse {
    private String orderCode;
    private LocalDateTime createdAt;
    private OrderState orderState;
    private BigDecimal totalPrice;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private String userName;
    private String shippingCity;
    private String shippingStreet;
    private String shippingDetail;
}
