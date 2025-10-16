package com.iremayvaz.controller;

import com.iremayvaz.model.dto.AddCartItemRequest;
import com.iremayvaz.model.dto.CartRequest;
import com.iremayvaz.model.entity.Cart;
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

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddCartItemRequest cartItem){
        return cartService.addToCart(cartItem);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable(value = "id") @NotNull Long id) {
        return cartService.removeFromCart(id);
    }

    @GetMapping("/view")
    public ResponseEntity<String> viewCart(@RequestBody CartRequest cart){
        return cartService.viewCart(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestBody CartRequest cart){
        return cartService.clearCart(cart);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCartItem(@PathVariable(value = "id") @NotNull Long id){
        return cartService.updateCartItem(id);
    }

}
