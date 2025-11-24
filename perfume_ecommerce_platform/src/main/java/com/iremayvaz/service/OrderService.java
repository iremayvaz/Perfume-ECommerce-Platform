package com.iremayvaz.service;

import com.iremayvaz.model.dto.PlaceOrderRequest;
import com.iremayvaz.model.dto.PlaceOrderResponse;
import com.iremayvaz.model.entity.CartItem;
import com.iremayvaz.model.entity.Order;
import com.iremayvaz.model.entity.OrderItem;
import com.iremayvaz.repository.CartRepository;
import com.iremayvaz.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;

    @Transactional
    public PlaceOrderResponse placeOrder(Long user_id, PlaceOrderRequest placeOrderRequest){
        var cart = cartRepository.findCartByUserId(user_id) // Kullanıcının sepetini bul
                .orElseThrow(() -> new NoSuchElementException("Kullanıcıya ait sepet bulunamadı!"));

        if(cart.getCartItems().isEmpty()){ // Sepet boş
            throw new IllegalStateException("Sepetiniz boş.");
        }

        // Ödeme alınacak -> Stok düş
        cart.getCartItems().forEach(cartItem -> {
            productService.decreaseStock(cartItem.getProduct().getId(),
                                         cartItem.getQuantity());
        });

        // Sipariş oluştur
        Order newOrder = new Order();
        newOrder.setUser(cart.getUser()); // Kimin siparişi

        // CartItem bilgileri ile OrderItem bilgilerini eşitliyoruz
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(newOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPriceSnapshot(cartItem.getUnitPriceSnapshot());

            newOrder.getOrderItems().add(orderItem);
        }

        // Sipariş adresi
        newOrder.setShippingCity(placeOrderRequest.getShippingCity());
        newOrder.setShippingStreet(placeOrderRequest.getShippingStreet());
        newOrder.setShippingDetail(placeOrderRequest.getShippingDetail());

        // Sipariş kodu oluştur
        newOrder.setOrderCode(generateOrderCode());

        // Sipariş oluşturulma saati
        newOrder.setCreatedAt(LocalDateTime.now());

        // siparişi kaydet
        Order savedOrder = orderRepository.save(newOrder);

        // Sipariş oluşturuldu -> sepeti boşalt
        cart.getCartItems().clear();
        cartRepository.save(cart);

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse(newOrder.getOrderCode()); // Sipariş cevabı

        return placeOrderResponse;
    }

    // Sipariş kodu
    private String generateOrderCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20251124
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // AX3F7Q
        return "ORD-" + datePart + "-" + randomPart; // ORD-20251124-AX3F7Q
    }

}
