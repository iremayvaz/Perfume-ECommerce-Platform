package com.iremayvaz.model.dto.response;

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
    private Long id;
    private String code;
    private LocalDateTime createdAt;
    private OrderState state;
    private BigDecimal totalPrice;
}
