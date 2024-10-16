package com.d2csgame.server.warehouse;

import com.d2csgame.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends JpaRepository<Account, Long> {

    @Query("select sum (quantity) from AccountProduct where product.id = :productId")
    int getQuantityByProductId(Long productId);

}
