package com.d2csgame.server.warehouse.service.impl;

import com.d2csgame.entity.Warehouse;
import com.d2csgame.exception.ResourceNotFoundException;
import com.d2csgame.server.warehouse.WarehouseRepository;
import com.d2csgame.server.warehouse.service.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private WarehouseRepository repository;

    @Override
    public Warehouse getWarehouseById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Warehouse Not Found"));
    }
}
