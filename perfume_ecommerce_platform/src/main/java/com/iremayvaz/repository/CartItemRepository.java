package com.iremayvaz.repository;

import com.iremayvaz.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    @Query("""
    select ci 
    from CartItem ci 
    where ci.cart.user.id = :userId and ci.id = :itemId
    """)
    Optional<CartItem> findByUserIdAndItemId(@Param("userId")Long userId,
                                             @Param("itemId")Long cartItemId);
    void deleteByCartId(Long userId);
}
