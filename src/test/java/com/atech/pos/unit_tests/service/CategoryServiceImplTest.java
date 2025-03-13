package com.atech.pos.unit_tests.service;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.entity.Category;
import com.atech.pos.mappers.CategoryMapper;
import com.atech.pos.mappers.CategoryUpsertDtoMapper;
import com.atech.pos.repository.CategoryRepository;
import com.atech.pos.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryUpsertDtoMapper categoryUpsertDtoMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category category;
    CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category("Fast Food");
        categoryDto = new CategoryDto("fast food");
    }

    @Test
    void getAllCategories() {
    }

    @Test
    void findCategoryById() {
    }

    @Test
    void findCategoryByName() {
    }

    @Test
    void createCategoryShouldCreate() {

        CategoryUpsertDto categoryUpsertDto = new CategoryUpsertDto(null, "fast food");
        category.setId(UUID.randomUUID().toString());

        when(categoryRepository.findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName()))
                .thenReturn(Optional.empty());

        when(categoryUpsertDtoMapper.mapToEntity(categoryUpsertDto)).thenReturn(category);

        when(categoryRepository.save(category)).thenReturn(category);

        String id = categoryService.createCategory(categoryUpsertDto);

        assertThat(id).isEqualTo(category.getId());
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}