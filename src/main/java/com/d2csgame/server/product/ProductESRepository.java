package com.d2csgame.server.product;

import com.d2csgame.entity.ProductES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductESRepository extends ElasticsearchRepository<ProductES, Long> {
    
    // Example method for custom search
    List<ProductES> findByName(String name);

    // Example method for full-text search on description
    List<ProductES> findByDescriptionContaining(String description);


}
