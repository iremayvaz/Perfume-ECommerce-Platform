package com.iremayvaz.model.dto.response;

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
    private String code;
    private LocalDateTime createdAt;
    private OrderState state;
    private BigDecimal totalPrice;
    private List<OrderItemDto> items = new ArrayList<>();
    private String userName;
    private String shippingCity;
    private String shippingStreet;
    private String shippingDetail;
}
