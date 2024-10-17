package com.d2csgame.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.d2csgame.entity.ProductES;
import com.d2csgame.utils.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class ElasticsearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public SearchResponse<Map> matchAllServices() {
        try {
            Supplier<Query> supplier = ElasticSearchUtil.supplier();
            SearchResponse<Map> searchResponse = elasticsearchClient.search(s -> s.query(supplier.get()), Map.class);
            System.out.println("ElasticSearch Query is: " + supplier.get().toString());
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchResponse<ProductES> matchAllProductsServices() {
        try {
            Supplier<Query> supplier = ElasticSearchUtil.supplier();
            SearchResponse<ProductES> searchResponse = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), ProductES.class);
            System.out.println("ElasticSearch Query is: " + supplier.get().toString());
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchResponse<ProductES> matchProductsWithName(String fieldValue) {
        try {
            Supplier<Query> supplier = ElasticSearchUtil.supplierWithNameField(fieldValue);
            SearchResponse<ProductES> searchResponse = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), ProductES.class);
            System.out.println("ElasticSearch Query is: " + supplier.get().toString());
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchResponse<ProductES> boolQueryImpl(String productName, Integer quantity) {
        try {
            Supplier<Query> supplier = ElasticSearchUtil.supplierQueryForBoolQuery(productName, quantity);
            SearchResponse<ProductES> searchResponse = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), ProductES.class);
            System.out.println("ElasticSearch Query is: " + supplier.get().toString());
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchResponse<ProductES> fuzzySearch(String approximateProductName) {
        try {
            Supplier<Query> supplier = ElasticSearchUtil.supplierFuzzyQuery(approximateProductName);
            SearchResponse<ProductES> searchResponse = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), ProductES.class);
            System.out.println("ElasticSearch Fuzzy Query is: " + supplier.get().toString());
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchResponse<ProductES> multiMatch(String key, List<String> fields) {
        try {
            Supplier<Query> supplier = ElasticSearchUtil.supplierMultiMatch(key, fields);
            SearchResponse<ProductES> searchResponse = elasticsearchClient.search(s -> s.index("products").query(supplier.get()), ProductES.class);
            System.out.println("ElasticSearch MultiMatch Query is: " + supplier.get().toString());
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
