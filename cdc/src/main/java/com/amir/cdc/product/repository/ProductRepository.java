package com.amir.cdc.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amir.cdc.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    void removeProductByMongoId(String mongoId);

    Product findByMongoId(String mongoId);
}