package com.iremayvaz.model.dto;

import com.iremayvaz.model.enums.OrderState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ViewOrdersResponse {
    private String orderCode;
    private LocalDateTime createdAt;
    private OrderState orderState;
    private BigDecimal totalPrice;
}
