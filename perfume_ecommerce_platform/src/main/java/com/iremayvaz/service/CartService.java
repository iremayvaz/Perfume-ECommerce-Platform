package com.iremayvaz.service;

import com.iremayvaz.model.dto.AddToCartRequest;
import com.iremayvaz.model.dto.AddCartItemResponse;
import com.iremayvaz.model.dto.AddToCartResponse;
import com.iremayvaz.model.entity.Cart;
import com.iremayvaz.model.entity.CartItem;
import com.iremayvaz.repository.CartItemRepository;
import com.iremayvaz.repository.CartRepository;
import com.iremayvaz.repository.ProductRepository;
import com.iremayvaz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public AddToCartResponse addToCart(Long user_id, AddToCartRequest addToCartRequest){
        // Kullanıcıya ait böyle bir sepet var mı?
        var cart = cartRepository.findCartByUserId(user_id)
                .orElseGet(() -> { // Yoksa oluştur.
                    // Kullanıcı var mı?
                    var user = userRepository.findById(user_id)
                            .orElseThrow(() -> new IllegalArgumentException("Kayıtlı kullanıcı bulunamadı: " + user_id));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // Böyle bir ürün var mı?
        var product = productRepository.findById(addToCartRequest.getProduct_id())
                .orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı: " + addToCartRequest.getProduct_id()));

        if (addToCartRequest.getQuantity() == null || addToCartRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity > 0 olmalı.");
        }

        // KULLANICI VAR.
        // KULLANICIYA AİT SEPET VAR.
        // KULLANICININ ALMAK İSTEDİĞİ ÜRÜN VAR
        // Peki kullanıcının sepetinde aynı üründen var mı?
        var cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        log.info("BEFORE ADDING - cartItemId={}, version={}, qty={}, thread={}",
                cartItem.getId(), cartItem.getVersion(), cartItem.getQuantity(), Thread.currentThread().getName());

        // Aynı üründen sepette yok. Yeni ekleniyor.
        if(cartItem == null){
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(addToCartRequest.getQuantity()); // 1
            cartItem.setUnitPriceSnapshot(product.getPrice());
            cartItem.setPriceLockedUntil(LocalDateTime.now().plusDays(1));

            cart.addItem(cartItem);
        } else { // Aynı ürün var
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.getQuantity());

            if(cartItem.getPriceLockedUntil() != null
                    && LocalDateTime.now().isAfter(cartItem.getPriceLockedUntil())){
                cartItem.setPriceLockedUntil(LocalDateTime.now().plusDays(1));
                cartItem.setUnitPriceSnapshot(product.getPrice());
            }
        }

        cartRepository.save(cart);

        log.info("AFTER ADDING  - cartItemId={}, version={}, qty={}, thread={}",
                cartItem.getId(), cartItem.getVersion(), cartItem.getQuantity(), Thread.currentThread().getName());

        return toDto(cart);
    }

    public String removeFromCart(Long item_id) {
        return null;
    }

    public String viewCart(Long user_id){
        return null;
    }

    @Transactional
    public String clearCart(Long user_id){ // Sepeti temizle
        cartRepository.findCartByUserId(user_id).get() // Sepeti bul
                    .getCartItems() // Sepetteki ürün listesini al
                    .clear(); // Temizle
        return "Silindi.";
    }

    public String updateCartItem(Long item_id){
        return null;
    }

    private AddToCartResponse toDto(Cart cart) {
        List<AddCartItemResponse> items = cart.getCartItems().stream().map(ci -> {
                    BigDecimal line = ci.getUnitPriceSnapshot()
                    .multiply(BigDecimal.valueOf(ci.getQuantity()));
            return new AddCartItemResponse(
                    ci.getId(),
                    ci.getProduct().getId(),
                    ci.getProduct().getProductName(),
                    ci.getQuantity(),
                    ci.getUnitPriceSnapshot(),
                    line,
                    ci.getPriceLockedUntil(),
                    ci.getCurrency()
            );
        }).toList();

        BigDecimal total = items.stream()
                .map(AddCartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AddToCartResponse(cart.getId(), items, total, false);
    }
}
