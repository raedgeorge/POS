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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        category = new Category("fast food");
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

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());
        Category savedCategory = captor.getValue();

        assertThat(savedCategory.getCategoryName()).isEqualTo("Fast Food");

        assertThat(id).isEqualTo(category.getId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}