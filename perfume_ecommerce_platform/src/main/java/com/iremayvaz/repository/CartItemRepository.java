package com.iremayvaz.repository;

import com.iremayvaz.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    @Query("""
        select ci from CartItem ci
        where ci.id = :itemId
          and ci.cart.user.id = :userId
    """)
    Optional<CartItem> findForUser(@Param("userId") Long userId,
                                   @Param("itemId") Long itemId);

    @Modifying
    @Query("""
    delete from CartItem ci
    where ci.id = :itemId and ci.cart.user.id = :userId
    """)
    int deleteForUser(@Param("userId") Long userId, @Param("itemId") Long itemId);

}
