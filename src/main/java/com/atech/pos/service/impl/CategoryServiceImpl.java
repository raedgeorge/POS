package com.atech.pos.service.impl;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.entity.Category;
import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.mappers.CategoryMapper;
import com.atech.pos.mappers.CategoryUpsertDtoMapper;
import com.atech.pos.repository.CategoryRepository;
import com.atech.pos.service.CategoryService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryUpsertDtoMapper categoryUpsertDtoMapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToDto)
                .toList();
    }

    @Override
    public CategoryDto findCategoryById(String categoryId) {

        return categoryRepository.findById(categoryId)
                .map(categoryMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
    }

    @Override
    public CategoryDto findCategoryByName(String categoryName) {

        return categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .map(categoryMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Name", categoryName));
    }

    @Override
    public String createCategory(CategoryUpsertDto categoryUpsertDto) {

        checkIfCategoryExistsThrowException(categoryUpsertDto.categoryName());

        Category category = categoryUpsertDtoMapper.mapToEntity(categoryUpsertDto);
        category.setCategoryName(convertEachWorldToFirstLetterUpperCase(category.getCategoryName()));
        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getId();
    }

    @Override
    public CategoryDto updateCategory(CategoryUpsertDto categoryUpsertDto) {

        if (ObjectUtils.isEmpty(categoryUpsertDto.id()))
            throw new ValidationException("Category Id field is required");

        Category category = categoryRepository.findById(categoryUpsertDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryUpsertDto.id()));

        category.setLastModified(LocalDateTime.now());
        category.setCategoryName(convertEachWorldToFirstLetterUpperCase(categoryUpsertDto.categoryName()));
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.mapToDto(updatedCategory);
    }

    @Override
    public boolean deleteCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

        categoryRepository.delete(category);

        return true;
    }

    // *********************** Private methods *********************** \\
    private void checkIfCategoryExistsThrowException(String categoryName) {

        categoryRepository.findByCategoryNameIgnoreCase(categoryName.trim())
                .ifPresent(category -> {
                    throw new ResourceExistsException("Category", "Name", categoryName);
                });
    }

    private String convertEachWorldToFirstLetterUpperCase(String text){

        StringBuilder stringBuilder = new StringBuilder();

        String[] wordsArray = text.trim().split(" ");

        int index = 0;

        for (String str : wordsArray){
            stringBuilder.append(convertWorldToFirstLetterUpperCase(str));

            if (index != wordsArray.length - 1){
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
