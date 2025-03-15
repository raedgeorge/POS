package com.atech.pos.repository;

import com.atech.pos.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByProductNameIgnoreCase(String productName);

    Optional<Product> findByProductNameIgnoreCaseAndId(String productName, String productId);
}
