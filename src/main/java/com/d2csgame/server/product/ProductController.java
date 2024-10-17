package com.d2csgame.server.product;

import com.d2csgame.config.Translator;
import com.d2csgame.model.response.ResponseData;
import com.d2csgame.server.product.model.request.CreateProductReq;
import com.d2csgame.server.product.model.request.EditProductReq;
import com.d2csgame.server.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseData<?> getAllProducts(@PageableDefault Pageable pageable) {
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("product.homepage.list.found"), productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseData<?> getProductById(@PathVariable Long id) {
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("product.detail.found"), productService.getProductById(id));
    }

    @PostMapping()
    public ResponseData<?> createProduct(@Valid CreateProductReq req) throws IOException {
        productService.createProduct(req);
        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("product.create.success"));
    }

    @PutMapping()
    public ResponseData<?> editProduct(@Valid EditProductReq req) throws IOException {
        productService.editProduct(req);
        return new ResponseData<>(HttpStatus.FOUND.value(), Translator.toLocale("product.edit.success"));

    }

    @DeleteMapping("/{productId}")
    public ResponseData<?> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("product.delete.success"));
    }

    @GetMapping("/category")
    public ResponseData<?> getProductByTag(@RequestParam(value = "tagId", required = false, defaultValue = "") Long tagId,
                                           @RequestParam(value = "characterId", required = false, defaultValue = "") Long characterId,
                                           @PageableDefault Pageable pageable) {
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("product.list.filter.found"), productService.getProductByCategory(tagId, characterId, pageable));
    }
}
