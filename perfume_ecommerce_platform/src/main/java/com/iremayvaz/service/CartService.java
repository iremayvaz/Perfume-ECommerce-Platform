package com.iremayvaz.service;

import com.iremayvaz.model.dto.request.AddToCartRequest;
import com.iremayvaz.model.dto.response.AddCartItemResponse;
import com.iremayvaz.model.dto.response.AddToCartResponse;
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
import java.util.*;
import java.util.stream.Collectors;

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
        var cartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        CartItem cartItem = cartItemOpt.orElse(null);

        if (cartItem != null) {
            log.info("BEFORE ADDING - cartItemId={}, version={}, qty={}, thread={}",
                    cartItem.getId(), cartItem.getVersion(), cartItem.getQuantity(), Thread.currentThread().getName());
        }

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

    @Transactional
    // Sepetten bir ürünü tüm adetleriyle silmek (Çöp kutusu tuşuna basılıncaki davranış)
    public String removeFromCart(Long user_id, Long item_id) {
        var cartItem = cartItemRepository.findForUser(user_id, item_id)   // Kullanıcı id ve item id'ye göre ürünü bul
                .orElseThrow(() -> new IllegalArgumentException("No item in this cart"));

        String productName = cartItem.getProduct().getName();
        cartItemRepository.delete(cartItem); // ürünü sil

        return productName + " is removed from cart.";
    }

    // Sepetteki tüm ürünleri görüntüle
    public List<AddCartItemResponse> viewCart(Long user_id){
        var cart = cartRepository.findCartByUserId(user_id) // Kullanıcının sepetini bul
                .orElseThrow(() -> new IllegalArgumentException("Cart is not found"));
        AddToCartResponse addToCartResponse = toDto(cart);
        List<AddCartItemResponse> addCartItemResponse = addToCartResponse.getCartItems(); // Sepetteki ürünleri al
        return addCartItemResponse;
    }

    @Transactional
    // Tüm sepeti temizle
    public String clearCart(Long user_id){ // Sepeti temizle
        var cart = cartRepository.findCartByUserId(user_id) // Sepeti bul
                .orElseThrow(() -> new IllegalArgumentException("Cart is not found"));

        cart.getItems() // Sepetteki ürün listesini al
                .clear();   // Temizle

        return "Cart cleared.";
    }

    @Transactional
    // Sepetteki ürünü yeni miktarla güncelle
    public AddToCartResponse updateCartItem(Long user_id, Long item_id, int quantity){

        // Tüm sepeti görüntüleyeceğiz
        var cart = cartRepository.findCartByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException("No cart")); // sepeti bul

        var cartItem = cartItemRepository.findForUser(user_id, item_id)   // Kullanıcı id ve item id'ye göre ürünü bul
                .orElseThrow(() -> new IllegalArgumentException("No item in this cart"));

        int newQuantity = quantity; // Önceki miktar + yeni miktar

        // Front kısmında - veya + butonuna tıklanılmasına göre - veya + miktar gönderilecek!
        if (newQuantity <= 0) { // Mesela 1 ürün vardı eksilttim
            int deleted = cartItemRepository.deleteForUser(user_id, item_id);
            if (deleted == 0) throw new IllegalArgumentException("No item in this cart");
        } else {
            cartItem.setQuantity(newQuantity);
        }

        return toDto(cart);
    }

    // Sepette görüntülecek DTO
    private AddToCartResponse toDto(Cart cart) {
        List<AddCartItemResponse> items = cart.getItems().stream()
                .sorted(Comparator.comparing(CartItem::getId))
                .map(ci -> {
                    BigDecimal line = ci.getUnitPriceSnapshot()
                            .multiply(BigDecimal.valueOf(ci.getQuantity()));

                    return new AddCartItemResponse(
                            ci.getId(),
                            ci.getProduct().getId(),
                            ci.getProduct().getName(),
                            ci.getQuantity(),
                            ci.getUnitPriceSnapshot(),
                            line,
                            ci.getPriceLockedUntil(),
                            ci.getCurrency()
                    );
                })
                .toList();

        BigDecimal total = items.stream()
                .map(AddCartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AddToCartResponse(cart.getId(), items, total, false);
    }

}
