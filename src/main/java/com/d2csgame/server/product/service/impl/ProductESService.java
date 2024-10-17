package com.d2csgame.server.product.service.impl;

import com.d2csgame.entity.Product;
import com.d2csgame.entity.ProductES;
import com.d2csgame.server.product.ProductESRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductESService {
    private final ProductESRepository productESRepository;

    public List<ProductES> searchByName(String name) {
        return productESRepository.findByName(name);
    }

    public List<ProductES> searchByDescription(String description) {
        return productESRepository.findByDescriptionContaining(description);
    }

    public Iterable<ProductES> getProducts() {
        return productESRepository.findAll();
    }
}
