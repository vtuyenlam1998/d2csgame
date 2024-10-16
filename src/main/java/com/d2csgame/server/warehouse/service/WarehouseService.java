package com.d2csgame.server.warehouse.service;

import com.d2csgame.entity.Account;

public interface WarehouseService {
    Account getWarehouseById(Long id);
    int getQuantityByProductId(Long productId);
}
