package com.iremayvaz.controller;

import com.iremayvaz.model.dto.AddToCartRequest;
import com.iremayvaz.model.dto.AddToCartResponse;
import com.iremayvaz.service.CartService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class RestCartController {

    private final CartService cartService;

    @PostMapping("/add/to/{user_id}")
    public AddToCartResponse addToCart(@PathVariable(name = "user_id") Long user_id,
                                       @RequestBody AddToCartRequest cartItem){
        return cartService.addToCart(user_id, cartItem);
    }

    @DeleteMapping("/remove/{item_id}")
    public ResponseEntity<String> removeFromCart(@PathVariable(value = "item_id") @NotNull Long item_id) {
        cartService.removeFromCart(item_id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/view/{user_id}")
    public ResponseEntity<String> viewCart(@PathVariable(value = "user_id") @NotNull Long user_id){
        cartService.viewCart(user_id);
        return null;
    }

    @DeleteMapping("/clear/{user_id}")
    public ResponseEntity<String> clearCart(@PathVariable(value = "user_id") @NotNull Long user_id){
        cartService.clearCart(user_id);
        return null;
    }

    @PutMapping("/update/{item_id}")
    public ResponseEntity<String> updateCartItem(@PathVariable(value = "item_id") @NotNull Long item_id){
        cartService.updateCartItem(item_id);
        return null;
    }

}
