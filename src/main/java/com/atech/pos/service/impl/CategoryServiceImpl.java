package com.atech.pos.service.impl;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.entity.Category;
import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.mappers.CategoryMapper;
import com.atech.pos.mappers.CategoryUpsertDtoMapper;
import com.atech.pos.repository.CategoryRepository;
import com.atech.pos.service.CategoryService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryUpsertDtoMapper categoryUpsertDtoMapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        return List.of();
    }

    @Override
    public CategoryDto findCategoryById(String categoryId) {
        return null;
    }

    @Override
    public CategoryDto findCategoryByName(String categoryName) {
        return null;
    }

    @Override
    public String createCategory(CategoryUpsertDto categoryUpsertDto) {

        Category category = categoryUpsertDtoMapper.mapToEntity(categoryUpsertDto);

        checkIfCategoryExistsThrowException(categoryUpsertDto.categoryName());

        category.setCategoryName(convertEachWorldToFirstLetterUpperCase(category.getCategoryName()));

        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getId();
    }

    @Override
    public CategoryDto updateCategory(CategoryUpsertDto categoryUpsertDto) {
        return null;
    }

    @Override
    public boolean deleteCategory(String categoryId) {
        return false;
    }

    // *********************** Private methods *********************** \\

    private void checkIfCategoryExistsThrowException(String categoryName) {

        categoryRepository.findByCategoryName(categoryName.trim())
                .ifPresent(category -> {
                    throw new ResourceExistsException("Category", "Name", categoryName);
                });
    }

    private String convertEachWorldToFirstLetterUpperCase(String text){

        StringBuilder stringBuilder = new StringBuilder();

        String[] strings = text.trim().split(" ");

        int index = 0;

        for (String str : strings){
            stringBuilder.append(convertWorldToFirstLetterUpperCase(str));

            if (index != strings.length - 1){
                stringBuilder.append(" ");
            }
            index++;
        }

        return stringBuilder.toString();
    }

    private String convertWorldToFirstLetterUpperCase(String text){

        StringBuilder stringBuilder = new StringBuilder();

        String trimmedString = text.trim();

        char firstChar = trimmedString.toUpperCase().charAt(0);

        char[] chars = trimmedString.toLowerCase().toCharArray();

        int index = 0;

        for (char ch : chars){
            if (index == 0){
                stringBuilder.append(firstChar);
                index++;
            } else {
                stringBuilder.append(ch);
            }
        }

        return stringBuilder.toString();
    }
}
