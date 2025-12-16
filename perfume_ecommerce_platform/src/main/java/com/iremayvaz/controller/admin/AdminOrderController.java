package com.iremayvaz.controller.admin;

import com.iremayvaz.model.entity.Order;
import com.iremayvaz.model.enums.OrderState;
import com.iremayvaz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    // Tüm siparişleri listele
    // Liste + filtre aynı endpoint
    @GetMapping
    public String listOrders(@RequestParam(required = false) String q, Model model) {

        List<Order> orders = (q == null || q.isBlank())
                ? orderService.getAllOrders()
                : orderService.filterOrders(q);

        model.addAttribute("orders", orders);
        model.addAttribute("q", q); // input'ta yazdığın kalsın
        return "admin/order-list";
    }

    // Sipariş Detayını ve Durumunu Göster
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        // Detay için mevcut viewOrderDetails metodunu veya direkt findById kullanabilirsin
        // Burada kolaylık olsun diye entity'i direkt çekiyorum varsayalım:
        Order order = orderService.getOrderById(id); // Bunu service'e eklemen gerekebilir

        model.addAttribute("order", order);
        model.addAttribute("allStates", OrderState.values()); // Select kutusu için tüm durumlar
        return "admin/order-detail";
    }

    // Sipariş Durumunu Güncelle (Kargoya Verildi vs.)
    @PostMapping("/update-state")
    public String updateState(@RequestParam Long orderId, @RequestParam OrderState state) {
        orderService.updateOrderState(orderId, state);
        return "redirect:/admin/orders/" + orderId; // Detay sayfasına geri dön
    }

    // Sipariş kodun, durumuna ve sahibine göre filtrele
    @GetMapping(params = {"content"})
    public String filterOrders(@RequestParam(required = false) String content,
                               Model model) {
        if (content == null || content.isBlank()) {
            model.addAttribute("orders", orderService.getAllOrders());
        } else {
            model.addAttribute("orders", orderService.filterOrders(content));
        }
        return "admin-orders";
    }
}