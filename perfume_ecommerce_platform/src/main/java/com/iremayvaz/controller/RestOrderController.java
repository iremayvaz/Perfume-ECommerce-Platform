package com.iremayvaz.controller;

import com.iremayvaz.model.dto.PlaceOrderRequest;
import com.iremayvaz.model.dto.PlaceOrderResponse;
import com.iremayvaz.service.CartService;
import com.iremayvaz.service.OrderService;
import com.iremayvaz.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class RestOrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;

    @PostMapping("/{user_id}")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@PathVariable(name = "user_id") Long user_id,
                                                         @RequestBody PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(user_id, placeOrderRequest);
        return ResponseEntity.status(201).body(placeOrderResponse); // 201 : CREATED
    }
}
