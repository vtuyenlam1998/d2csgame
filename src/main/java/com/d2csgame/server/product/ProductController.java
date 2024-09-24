package com.d2csgame.server.product;

import com.d2csgame.model.response.ResponseData;
import com.d2csgame.model.response.ResponseError;
import com.d2csgame.server.product.model.request.CreateProductReq;
import com.d2csgame.server.product.model.request.EditProductReq;
import com.d2csgame.server.product.model.response.MainProductRes;
import com.d2csgame.server.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseData<?> getAllProducts(@PageableDefault Pageable pageable) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "list of product in homepage", productService.findAll(pageable));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @GetMapping("/{id}")
    public ResponseData<?> getProductById(@PathVariable Long id) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "product detail", productService.getProductById(id));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @PostMapping()
    public ResponseData<?> createProduct(@Valid CreateProductReq req) {
        try {
            productService.createProduct(req);
            return new ResponseData<>(HttpStatus.CREATED.value(), "product created");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @PutMapping()
    public ResponseData<?> editProduct(@Valid EditProductReq req) {
        try {
            productService.editProduct(req);
            return new ResponseData<>(HttpStatus.FOUND.value(), "product edited");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseData<?> deleteProduct(@PathVariable("productId") Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "product deleted");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    @GetMapping("/category")
    public ResponseData<?> getProductByTag(@RequestParam(value = "tagId", required = false, defaultValue = "") Long tagId,
                                           @RequestParam(value = "characterId", required = false, defaultValue = "") Long characterId,
                                           @PageableDefault Pageable pageable) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "list of product filter by category", productService.getProductByCategory(tagId, characterId, pageable));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }
}
