package com.iremayvaz.controller.admin;

import com.iremayvaz.model.entity.Order;
import com.iremayvaz.model.enums.OrderState;
import com.iremayvaz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    // 1. Tüm siparişleri listele
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order-list";
    }

    // 2. Sipariş Detayını ve Durumunu Göster
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        // Detay için mevcut viewOrderDetails metodunu veya direkt findById kullanabilirsin
        // Burada kolaylık olsun diye entity'i direkt çekiyorum varsayalım:
        Order order = orderService.getOrderById(id); // Bunu service'e eklemen gerekebilir

        model.addAttribute("order", order);
        model.addAttribute("allStates", OrderState.values()); // Select kutusu için tüm durumlar
        return "admin/order-detail";
    }

    // 3. Sipariş Durumunu Güncelle (Kargoya Verildi vs.)
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam OrderState status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders/" + orderId; // Detay sayfasına geri dön
    }
}