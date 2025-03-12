package com.atech.pos.repository;

import com.atech.pos.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByCategoryName(String categoryName);
}
