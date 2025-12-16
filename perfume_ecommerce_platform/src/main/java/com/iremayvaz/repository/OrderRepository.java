package com.iremayvaz.repository;

import com.iremayvaz.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);

    // Tüm siparişlerin totalPrice alanlarını toplar
    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    Double sumTotalSales();

    // İstersen sadece "Tamamlanan" siparişleri toplamak için:
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.state = 'DELIVERED'")
    Double sumCompletedSales();
}
