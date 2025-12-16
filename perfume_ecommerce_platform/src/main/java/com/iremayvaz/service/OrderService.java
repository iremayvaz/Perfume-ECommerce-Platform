package com.iremayvaz.service;

import com.iremayvaz.model.dto.request.PlaceOrderRequest;
import com.iremayvaz.model.dto.response.*;
import com.iremayvaz.model.entity.CartItem;
import com.iremayvaz.model.entity.Order;
import com.iremayvaz.model.entity.OrderItem;
import com.iremayvaz.model.entity.Product;
import com.iremayvaz.model.enums.OrderState;
import com.iremayvaz.repository.CartRepository;
import com.iremayvaz.repository.OrderRepository;
import com.iremayvaz.repository.specs.OrderSpecifications;
import com.iremayvaz.repository.specs.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        if(cart.getItems().isEmpty()){ // Sepet boş
            throw new IllegalStateException("Sepetiniz boş.");
        }

        // Ödeme alınacak -> Stok düş
        cart.getItems().forEach(cartItem -> {
            productService.decreaseStock(cartItem.getProduct().getId(),
                                         cartItem.getQuantity());
        });

        // Sipariş oluştur
        Order newOrder = new Order();
        newOrder.setUser(cart.getUser()); // Kimin siparişi

        BigDecimal totalPrice = BigDecimal.ZERO; // Sipariş başlangıç tutarı

        // CartItem bilgileri ile OrderItem bilgilerini eşitliyoruz
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(newOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPriceSnapshot(cartItem.getUnitPriceSnapshot());

            newOrder.getItems().add(orderItem);

            // satır tutarı = birim fiyat * adet
            BigDecimal lineTotal = cartItem.getUnitPriceSnapshot()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            // sipariş toplamına ekle
            totalPrice = totalPrice.add(lineTotal);
        }

        // Sipariş adresi
        newOrder.setShippingCity(placeOrderRequest.getShippingCity());
        newOrder.setShippingStreet(placeOrderRequest.getShippingStreet());
        newOrder.setShippingDetail(placeOrderRequest.getShippingDetail());

        // Sipariş kodu oluştur
        newOrder.setCode(generateOrderCode());

        // Sipariş oluşturulma saati
        newOrder.setCreatedAt(LocalDateTime.now());

        // Sipariş toplam tutarı
        newOrder.setTotalPrice(totalPrice);

        newOrder.setState(OrderState.CREATED);

        // siparişi kaydet
        Order savedOrder = orderRepository.save(newOrder);

        // Sipariş oluşturuldu -> sepeti boşalt
        cart.getItems().clear();
        cartRepository.save(cart);

        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse(newOrder.getCode(),
                                                                        newOrder.getState(),
                                                                        newOrder.getTotalPrice()); // Sipariş cevabı

        return placeOrderResponse;
    }

    public List<ViewOrdersResponse> viewOrders(Long user_id){
        List<Order> orders = orderRepository.findByUser_IdOrderByCreatedAtDesc(user_id);
        return toViewOrdersResponse(orders);
    }

    public ViewOrderDetailResponse viewOrderDetails(Long order_id){
        var order = orderRepository.findById(order_id) // Siparişi bul
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı."));

        ViewOrderDetailResponse viewOrderDetailResponse = new ViewOrderDetailResponse();
        viewOrderDetailResponse.setOrderCode(order.getCode());
        viewOrderDetailResponse.setCreatedAt(order.getCreatedAt());
        viewOrderDetailResponse.setOrderState(order.getState());
        viewOrderDetailResponse.setTotalPrice(order.getTotalPrice());
        viewOrderDetailResponse.setShippingCity(order.getShippingCity());
        viewOrderDetailResponse.setShippingStreet(order.getShippingStreet());
        viewOrderDetailResponse.setShippingDetail(order.getShippingDetail());
        viewOrderDetailResponse.setUserName(order.getUser().getFirstName() + " " + order.getUser().getLastName());

        for(OrderItem orderItem : order.getItems()) {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductName(orderItem.getProduct().getName());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setLineTotal(orderItem.getLineTotal());
            viewOrderDetailResponse.getOrderItems().add(orderItemDto);
        }

        return viewOrderDetailResponse;
    }

    // Sipariş kodu
    private String generateOrderCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20251124
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // AX3F7Q
        return "ORD-" + datePart + "-" + randomPart; // ORD-20251124-AX3F7Q
    }

    // ADMINSEL

    // Tüm siparişleri getir (Admin için)
    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // JPA Repository'nin hazır metodu
    }

    // Sipariş durumunu güncelle
    public void updateOrderState(Long orderId, OrderState newState) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        order.setState(newState);
        orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found!"));
    }

    public Long count() { return orderRepository.count(); }

    public Double sumTotalSales() { return orderRepository.sumTotalSales(); }

    public List<Order> filterOrders(String content) {
        var spec  = OrderSpecifications.search(content);
        return orderRepository.findAll(spec);
    }

    private List<ViewOrdersResponse> toViewOrdersResponse(List<Order> orders){
        List<ViewOrdersResponse> viewOrdersResponses = new ArrayList<>();

        for (Order order : orders) {
            ViewOrdersResponse viewOrdersResponse = new ViewOrdersResponse();
            viewOrdersResponse.setOrderCode(order.getCode());
            viewOrdersResponse.setCreatedAt(order.getCreatedAt());      // böyle bir field varsa ekleyebilirsin
            viewOrdersResponse.setOrderState(order.getState());    // istersen durum da
            viewOrdersResponse.setTotalPrice(order.getTotalPrice());  // ileride toplam tutar da eklenebilir
            viewOrdersResponses.add(viewOrdersResponse);
        }

        return viewOrdersResponses;
    }
}
