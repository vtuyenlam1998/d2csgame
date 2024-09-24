package com.d2csgame.server.product;

import com.d2csgame.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.character c " +
            "WHERE (:tagId IS NULL OR t.id = :tagId) " +
            "AND (:characterId IS NULL OR c.id = :characterId)")
    Page<Product> findByTagIdOrCharacterId(Long tagId, Long characterId, Pageable pageable);

}
