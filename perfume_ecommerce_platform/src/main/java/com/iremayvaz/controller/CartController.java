package com.iremayvaz.controller;

import com.iremayvaz.model.dto.AddCartItemRequest;
import com.iremayvaz.model.dto.AddCartItemResponse;
import com.iremayvaz.model.dto.CartDto;
import com.iremayvaz.service.CartService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/to/{user_id}")
    public CartDto addToCart(@PathVariable(name = "user_id") Long user_id,
                             @RequestBody AddCartItemRequest cartItem){
        return cartService.addToCart(user_id, cartItem);
    }

    @DeleteMapping("/remove/{item_id}")
    public ResponseEntity<String> removeFromCart(@PathVariable(value = "item_id") @NotNull Long item_id) {
        return cartService.removeFromCart(item_id);
    }

    @GetMapping("/view/{user_id}")
    public ResponseEntity<String> viewCart(@PathVariable(value = "user_id") @NotNull Long user_id){
        return cartService.viewCart(user_id);
    }

    @DeleteMapping("/clear/{user_id}")
    public ResponseEntity<String> clearCart(@PathVariable(value = "user_id") @NotNull Long user_id){
        return cartService.clearCart(user_id);
    }

    @PutMapping("/update/{item_id}")
    public ResponseEntity<String> updateCartItem(@PathVariable(value = "item_id") @NotNull Long item_id){
        return cartService.updateCartItem(item_id);
    }

}
