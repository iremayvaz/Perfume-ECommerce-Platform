package com.iremayvaz.controller;

import com.iremayvaz.model.dto.PlaceOrderRequest;
import com.iremayvaz.model.dto.PlaceOrderResponse;
import com.iremayvaz.model.dto.ViewOrderDetailResponse;
import com.iremayvaz.model.dto.ViewOrdersResponse;
import com.iremayvaz.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order API", description = "Sipariş işlemleri")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class RestOrderController {

    private final OrderService orderService;

    @Operation(description = "Sipariş oluştur")
    @PostMapping("/{user_id}")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@PathVariable(name = "user_id") Long user_id,
                                                         @RequestBody PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(user_id, placeOrderRequest);
        return ResponseEntity.status(201).body(placeOrderResponse); // 201: CREATED
    }

    @Operation(description = "Kullanıcının sipariş geçmişini gör")
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<ViewOrdersResponse>> viewOrders(@PathVariable(value = "user_id") @NotNull Long user_id) {
        var orders = orderService.viewOrders(user_id);
        return ResponseEntity.ok(orders);
    }

    @Operation(description = "Sipariş detayını gör")
    @GetMapping("/details/{order_id}")
    public ResponseEntity<ViewOrderDetailResponse> viewOrderDetails(@PathVariable(value = "order_id") @NotNull Long order_id) {
        var orderDetail = orderService.viewOrderDetails(order_id);
        return ResponseEntity.ok(orderDetail);
    }
}
