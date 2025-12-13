package com.iremayvaz.repository;

import com.iremayvaz.model.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
                                            JpaSpecificationExecutor<Product> {
    // Ben bu ürünü güncelleyeceğim, lock’lu okuyorum
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    // Stoğu 3 veya daha az olan ürünlerin sayısını döner
    long countByStockQuantityLessThanEqual(int quantity);

    // Stoğu 3 veya daha az olan ürünleri listeler
    List<Product> findByStockQuantityLessThanEqual(int quantity);
}

