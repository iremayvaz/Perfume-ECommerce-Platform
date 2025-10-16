package com.iremayvaz.service;

import com.iremayvaz.model.dto.AddCartItemRequest;
import com.iremayvaz.model.dto.CartRequest;
import com.iremayvaz.repository.CartRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public ResponseEntity<String> addToCart(@RequestBody AddCartItemRequest cartItem){
        return null;
    }

    public ResponseEntity<String> removeFromCart(@PathVariable(value = "id") @NotNull Long id) {
        return null;
    }

    public ResponseEntity<String> viewCart(@RequestBody CartRequest cart){
        return null;
    }

    public ResponseEntity<String> clearCart(@RequestBody CartRequest cart){
        return null;
    }

    public ResponseEntity<String> updateCartItem(@PathVariable(value = "id") @NotNull Long id){
        return null;
    }
}
