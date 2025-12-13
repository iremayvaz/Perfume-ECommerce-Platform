package com.iremayvaz.controller.admin;

import com.iremayvaz.repository.OrderRepository;
import com.iremayvaz.repository.ProductRepository;
import com.iremayvaz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/admin", "/admin/dashboard"}) // İki adrese de bakar
@RequiredArgsConstructor
public class AdminDashboardController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String showDashboard(Model model) {

        // Toplam Ciro (Eğer hiç sipariş yoksa null döner, 0 yapalım)
        Double totalSales = orderRepository.sumTotalSales();
        model.addAttribute("totalSales", totalSales != null ? totalSales : 0.0);

        // Toplam Sipariş Sayısı
        model.addAttribute("totalOrders", orderRepository.count());

        // Toplam Ürün Çeşidi
        model.addAttribute("totalProducts", productRepository.count());

        // Toplam Müşteri Sayısı
        model.addAttribute("totalUsers", userRepository.count());

        // Kritik stok (3 ve altı) olan ürün sayısı
        long lowStockCount = productRepository.countByStockQuantityLessThanEqual(3);
        model.addAttribute("lowStockCount", lowStockCount);

        // Kritik stoktaki ürünlerin listesini de gönderelim (Özet tablo için)
        model.addAttribute("lowStockProducts", productRepository.findByStockQuantityLessThanEqual(3));

        return "admin/dashboard";
    }
}