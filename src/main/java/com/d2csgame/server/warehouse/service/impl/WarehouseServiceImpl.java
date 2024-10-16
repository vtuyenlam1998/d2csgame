package com.d2csgame.server.warehouse.service.impl;

import com.d2csgame.entity.Account;
import com.d2csgame.exception.ResourceNotFoundException;
import com.d2csgame.server.warehouse.WarehouseRepository;
import com.d2csgame.server.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository repository;

    @Override
    public Account getWarehouseById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warehouse Not Found"));
    }

    @Override
    public int getQuantityByProductId(Long productId) {
        return repository.getQuantityByProductId(productId);
    }
}
