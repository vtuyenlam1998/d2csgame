package com.d2csgame.server.product;

import com.d2csgame.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.tags WHERE p.id = :id ")
    Optional<Product> findById(Long id);

    @Query("SELECT p FROM Product p " +
            "JOIN p.character c " +
            "LEFT JOIN p.tags t " +
            "WHERE (p.name LIKE CONCAT('%', :keyword, '%') " +
            "OR p.description LIKE CONCAT('%', :keyword, '%') " +
            "OR c.name LIKE CONCAT('%', :keyword, '%') " +
            "OR t.name LIKE CONCAT('%', :keyword, '%'))")
    List<Product> findAll(@Param("keyword") String keyword);
}
