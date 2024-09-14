package com.d2csgame.service.impl;

import com.d2csgame.server.product.model.response.MainProductRes;
import com.d2csgame.server.product.service.ProductService;
import com.d2csgame.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SearchServiceImpl implements SearchService {

//    @Autowired
//    private PostService postService;

    @Autowired
    private ProductService productService;

    private final String POST_KEY = "post";

    private final String PRODUCT_KEY = "product";

    @Override
    public Map<String, Object> searchGlobal(String keyword) {

        CompletableFuture<List<MainProductRes>> productFuture = this.searchAllProduct(keyword);
//        CompletableFuture<List<PostSearchRes>> componentFuture = this.searchAllPost(keyword);

        CompletableFuture.allOf(productFuture).join();

        List<MainProductRes> products = productFuture.join();

//        List<ComponentSearchRes> components = componentFuture.join();

        return Map.of(
                PRODUCT_KEY, products);
    }

    @Async
    public CompletableFuture<List<MainProductRes>> searchAllProduct(String keyword){
        return CompletableFuture.completedFuture(productService.findAll(keyword));
    }

//    @Async
//    public CompletableFuture<List<ComponentSearchRes>> searchAllPost(String keyword){
//        return CompletableFuture.completedFuture(componentSearchResMapper.toDto(componentService.findAll(keyword)));
//    }
}
