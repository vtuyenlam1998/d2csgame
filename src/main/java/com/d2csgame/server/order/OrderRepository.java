package com.d2csgame.server.order;

import com.d2csgame.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o left JOIN FETCH o.items WHERE o.user.id=:id")
    Page<Order> findAllByUserId(Long id, Pageable pageable);
}
