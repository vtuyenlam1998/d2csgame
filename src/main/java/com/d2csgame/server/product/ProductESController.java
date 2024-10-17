package com.d2csgame.server.product;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.d2csgame.entity.Image;
import com.d2csgame.entity.Product;
import com.d2csgame.entity.ProductES;
import com.d2csgame.server.image.model.response.ImageRes;
import com.d2csgame.server.image.service.ImageService;
import com.d2csgame.server.product.service.impl.ProductESService;
import com.d2csgame.service.impl.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productES")
@RequiredArgsConstructor
public class ProductESController {
    private final ProductESService productESService;
    private final ElasticsearchService elasticsearchService;
    private final ImageService imageService;

    @GetMapping()
    public Iterable<ProductES> findAll() {
        return productESService.getProducts();
    }

    @GetMapping("/matchAll")
    public String matchAll() {
        SearchResponse<Map> searchResponse = elasticsearchService.matchAllServices();
        return searchResponse.hits().hits().toString();
    }

    @GetMapping("/matchAllProducts")
    public List<ProductES> matchAllProducts() {
        SearchResponse<ProductES> searchResponse = elasticsearchService.matchAllProductsServices();
        return getListProductES(searchResponse);
    }

    @GetMapping("/matchAllProducts/{fieldValue}")
    public List<ProductES> matchAllProductsWithName(@PathVariable String fieldValue) {
        SearchResponse<ProductES> searchResponse = elasticsearchService.matchProductsWithName(fieldValue);
        return getListProductES(searchResponse);
    }

    @GetMapping("/boolQuery/{productName}/{quantity}")
    public List<ProductES> matchAllProductsWithName(@PathVariable String productName, @PathVariable Integer quantity) {
        SearchResponse<ProductES> searchResponse = elasticsearchService.boolQueryImpl(productName, quantity);
        return getListProductES(searchResponse);
    }

    @GetMapping("/fuzzySearch/{approximateProductname}")
    public List<ProductES> fuzzySearch(@PathVariable String approximateProductname) {
        SearchResponse<ProductES> searchResponse = elasticsearchService.fuzzySearch(approximateProductname);
        return getListProductES(searchResponse);
    }


    private List<ProductES> getListProductES(SearchResponse<ProductES> searchResponse) {
        List<Hit<ProductES>> listOfHits = searchResponse.hits().hits();
        List<ProductES> listOfProducts = new ArrayList<>();
        for (Hit<ProductES> hit : listOfHits) {
            listOfProducts.add(hit.source());
        }
        return listOfProducts;
    }

    @GetMapping("/multiMatch/{key}")
    public List<ProductES> multiMatch(@PathVariable String key, @RequestParam(value = "field", defaultValue = "name") List<String> fields) {
        SearchResponse<ProductES> searchResponse = elasticsearchService.multiMatch(key, fields);
        List<ProductES> productESList = getListProductES(searchResponse);
        return productESList.stream().peek(productES -> {
            ImageRes imageRes = imageService.getPrimaryImage(productES.getId());
            productES.setImageRes(imageRes);
        }).toList();
    }
}
