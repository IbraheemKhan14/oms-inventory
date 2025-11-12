package com.oms.inventory.repository;

import com.oms.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    Optional<Product> findByProductCode(String productCode);

    @Modifying
    @Query(value = "UPDATE products SET quantity = quantity - :qty WHERE product_code = :productCode AND quantity >= :qty", nativeQuery = true)
    int reserve(@Param("productCode") String productCode, @Param("qty") int qty);

    @Modifying
    @Query(value = "UPDATE products SET quantity = quantity + :qty WHERE product_code = :productCode", nativeQuery = true)
    void release(@Param("productCode") String productCode, @Param("qty") int qty);
}
