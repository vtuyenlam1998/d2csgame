package com.d2csgame.server.product.service;

import com.d2csgame.model.response.PageResponse;
import com.d2csgame.server.product.model.request.CreateProductReq;
import com.d2csgame.server.product.model.request.EditProductReq;
import com.d2csgame.server.product.model.response.DetailProductRes;
import com.d2csgame.server.product.model.response.MainProductRes;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    PageResponse<?> findAll(Pageable pageable);

    List<MainProductRes> findAll(String keyword);

    DetailProductRes getProductById(Long id);

    void createProduct(CreateProductReq req) throws IOException;

    void editProduct(EditProductReq req) throws IOException;

    void deleteProduct(Long productId);
}
