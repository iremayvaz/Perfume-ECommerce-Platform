package com.iremayvaz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AddToCartRequest {

    private Long cart_id;

    private Long product_id;

    private Integer quantity = 1;

}
