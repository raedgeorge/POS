package com.atech.pos.service;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

    CategoryDto findCategoryById(String categoryId);

    CategoryDto findCategoryByName(String categoryName);

    String createCategory(CategoryUpsertDto categoryUpsertDto);

    CategoryDto updateCategory(CategoryUpsertDto categoryUpsertDto);

    boolean deleteCategory(String categoryId);

    boolean categoryExistsById(String categoryId);
}
