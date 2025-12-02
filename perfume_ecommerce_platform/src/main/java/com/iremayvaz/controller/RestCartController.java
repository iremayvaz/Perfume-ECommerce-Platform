package com.iremayvaz.controller;

import com.iremayvaz.model.dto.AddCartItemResponse;
import com.iremayvaz.model.dto.AddToCartRequest;
import com.iremayvaz.model.dto.AddToCartResponse;
import com.iremayvaz.model.entity.CartItem;
import com.iremayvaz.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Cart API", description = "Sepet işlemleri")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class RestCartController {

    private final CartService cartService;

    @Operation(description = "Sepete ürün ekleme")
    @PostMapping("/add/to/{user_id}")
    public AddToCartResponse addToCart(@PathVariable(name = "user_id") Long user_id,
                                       @RequestBody AddToCartRequest cartItem){
        return cartService.addToCart(user_id, cartItem);
    }

    @Operation(description = "Sepetten ürün çıkarma")
    @DeleteMapping("/remove/{item_id}/from/{user_id}")
    public ResponseEntity<String> removeFromCart(@PathVariable(value = "item_id") @NotNull Long item_id,
                                                 @PathVariable(value = "user_id") @NotNull Long user_id) {
        String message = cartService.removeFromCart(user_id, item_id);
        return ResponseEntity.ok(message); // 200 + mesaj
    }

    @Operation(description = "Sepeti görüntüle")
    @GetMapping("/view/{user_id}")
    public ResponseEntity<Set<AddCartItemResponse>> viewCart(@PathVariable(value = "user_id") @NotNull Long user_id){
        var cartItems = cartService.viewCart(user_id);
        return ResponseEntity.ok(cartItems);
    }

    @Operation(description = "Sepeti temizle")
    @DeleteMapping("/clear/{user_id}")
    public ResponseEntity<String> clearCart(@PathVariable(value = "user_id") @NotNull Long user_id){
        String message = cartService.clearCart(user_id);
        return ResponseEntity.ok(message);
    }

    @Operation(description = "Sepetteki ürünü güncelle")
    @PutMapping("/update/{item_id}/to/{quantity}/for/{user_id}")
    public ResponseEntity<AddToCartResponse> updateCartItem(@PathVariable(value = "user_id")  @NotNull Long user_id,
                                                 @PathVariable(value = "item_id")  @NotNull Long item_id,
                                                 @PathVariable(value = "quantity") @NotNull int quantity){
        AddToCartResponse response = cartService.updateCartItem(user_id, item_id, quantity);
        return ResponseEntity.ok(response);
    }

}
