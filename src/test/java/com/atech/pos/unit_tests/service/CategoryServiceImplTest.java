package com.atech.pos.unit_tests.service;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.entity.Category;
import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.mappers.CategoryMapper;
import com.atech.pos.mappers.CategoryUpsertDtoMapper;
import com.atech.pos.repository.CategoryRepository;
import com.atech.pos.repository.ProductRepository;
import com.atech.pos.service.CategoryService;
import com.atech.pos.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryUpsertDtoMapper categoryUpsertDtoMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category category1;
    Category category2;
    List<Category> categories;

    CategoryDto categoryDto1;
    CategoryDto categoryDto2;
    List<CategoryDto> categoryDtos;

    @BeforeEach
    void setUp() {
        category1 = new Category("snacks");
        category2 = new Category("beverage");
        categories = Arrays.asList(category1, category2);

        categoryDto1 = new CategoryDto("snacks");
        categoryDto2 = new CategoryDto("beverage");
        categoryDtos = Arrays.asList(categoryDto1, categoryDto2);
    }

    @Test
    void getAllCategories_shouldReturnsDtoList_whenCategoriesExists() {

        // Arrange
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.mapToDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.mapToDto(category2)).thenReturn(categoryDto2);

        // Act
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

        // Assert
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).mapToDto(category1);
        verify(categoryMapper, times(1)).mapToDto(category2);

        assertThat(categoryDtoList.size()).isEqualTo(2);
        assertThat(categoryDtoList).containsExactly(categoryDto1, categoryDto2);
        assertThat(categoryDtoList.get(0).getCategoryName()).isEqualTo(categoryDto1.getCategoryName());
    }

    @Test
    void getAllCategories_shouldReturnEmptyDtoList_whenCategoriesNotExists() {

        // Arrange
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

        // Assert
        assertThat(categoryDtoList).isEmpty();
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findCategoryById_shouldReturnCategoryDto_whenIdExists() {

        // Arrange
        String categoryId = UUID.randomUUID().toString();

        when(categoryMapper.mapToDto(category1)).thenReturn(categoryDto1);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));

        // Act
        CategoryDto categoryDto = categoryService.findCategoryById(categoryId);

        // Assert
        verify(categoryMapper, times(1)).mapToDto(category1);
        verify(categoryRepository, times(1)).findById(categoryId);

        assertThat(categoryDto.getCategoryName()).isEqualTo(categoryDto1.getCategoryName());
    }

    @Test
    void findCategoryById_shouldThrowException_whenIdNotExist(){

        // Arrange
        String categoryId = UUID.randomUUID().toString();
        String expectedErrorMessage = "Category not found for Id: %s".formatted(categoryId);

        when(categoryRepository.findById(categoryId))
                .thenThrow(new ResourceNotFoundException("Category", "Id", categoryId));

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                              () -> categoryService.findCategoryById(categoryId));

        // Assert
        verify(categoryRepository, times(1)).findById(categoryId);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void findCategoryByName_shouldReturnCategoryDto_whenCategoryNameExist() {

        // Arrange
        String categoryName = "snacks";

        when(categoryMapper.mapToDto(category1)).thenReturn(categoryDto1);
        when(categoryRepository.findByCategoryNameIgnoreCase(categoryName)).thenReturn(Optional.of(category1));

        // Act
        CategoryDto categoryDto = categoryService.findCategoryByName(categoryName);

        // Assert
        verify(categoryMapper, times(1)).mapToDto(category1);
        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(categoryName);

        assertThat(categoryDto).isNotNull();
        assertThat(categoryDto.getCategoryName()).isEqualTo(categoryName);
    }

    @Test
    void findCategoryByName_shouldThrowException_whenCategoryNameNotExist() {

        // Arrange
        String categoryName = "snack";
        String expectedErrorMessage = "Category not found for Name: %s".formatted(categoryName);

        when(categoryRepository.findByCategoryNameIgnoreCase(categoryName))
                .thenThrow(new ResourceNotFoundException("Category", "Name", categoryName));

        // Act
        Exception exception = assertThrows(ResourceNotFoundException.class,
                              () -> categoryService.findCategoryByName(categoryName));

        // Assert
        verify(categoryRepository, times(1)).findByCategoryNameIgnoreCase(categoryName);

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void createCategory_shouldCreate_whenCategoryNameNotUsed() {

        // Arrange
        CategoryUpsertDto categoryUpsertDto = new CategoryUpsertDto(null, "food");

        Category category = new Category(categoryUpsertDto.categoryName());
        category.setId(UUID.randomUUID().toString());

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryUpsertDtoMapper.mapToEntity(categoryUpsertDto)).thenReturn(category);

        when(categoryRepository.findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName()))
                               .thenReturn(Optional.empty());

        // Act
        String createdCategoryId = categoryService.createCategory(categoryUpsertDto);

        // Assert
        verify(categoryRepository, times(1)).save(category);

        verify(categoryRepository, times(1))
                .findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName());

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());
        Category savedCategory = captor.getValue();

        assertThat(createdCategoryId).isEqualTo(category.getId());
        assertThat(savedCategory.getCategoryName()).isEqualTo("Food");
    }

    @Test
    void createCategory_shouldThrowException_whenCategoryNameUsed(){

        // Arrange
        CategoryUpsertDto categoryUpsertDto = new CategoryUpsertDto(null, "cleaning");

        when(categoryRepository.findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName()))
                .thenThrow(new ResourceExistsException("Category", "Name", categoryUpsertDto.categoryName()));

        // Act
        Exception exception = assertThrows(ResourceExistsException.class,
                              () -> categoryService.createCategory(categoryUpsertDto));

        // Assert
        verify(categoryRepository, times(1))
                .findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName());

        verify(categoryRepository, times(0)).save(new Category());

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(ResourceExistsException.class);
        assertThat(exception.getMessage())
                  .isEqualTo("Category exists for Name: %s".formatted(categoryUpsertDto.categoryName()));
    }

    @Test
    void updateCategory_shouldUpdate() {

        // Arrange
        String categoryId = UUID.randomUUID().toString();
        category1.setId(categoryId);

        CategoryUpsertDto categoryUpsertDto = new CategoryUpsertDto(categoryId, categoryDto1.getCategoryName());

        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        when(categoryRepository.findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName()))
                               .thenReturn(Optional.of(category1));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));

        when(categoryRepository.save(category1)).thenReturn(category1);

        when(categoryMapper.mapToDto(category1)).thenReturn(categoryDto1);

        // Act
        categoryService.updateCategory(categoryUpsertDto);

        // Assert
        verify(categoryRepository, times(1)).save(category1);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1))
                .findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName());

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());
        Category updatedCategory = captor.getValue();

        assertThat(updatedCategory).isNotNull();
    }

    @Test
    void updateCategory_shouldThrowException_whenUpdatedCategoryNewNameIsUsed() {

        // Arrange
        category2.setId(UUID.randomUUID().toString());

        String categoryId = UUID.randomUUID().toString();
        category1.setId(categoryId);

        CategoryUpsertDto categoryUpsertDto = new CategoryUpsertDto(categoryId, categoryDto1.getCategoryName());

        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        when(categoryRepository.findByCategoryNameIgnoreCase(categoryUpsertDto.categoryName()))
                               .thenReturn(Optional.of(category2));

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class,
                              () -> categoryService.updateCategory(categoryUpsertDto));

        // Assert
        verify(categoryRepository, times(0)).save(category1);
        verify(categoryRepository, times(0)).findById(categoryId);

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getMessage()).isEqualTo(
                "Category [%s] already exists".formatted(categoryUpsertDto.categoryName()));
    }

    @Test
    void deleteCategory_shouldSucceed_whenCategoryIdExist_andCategoryHasNoAssignedProducts() {

        // Arrange
        String categoryId = UUID.randomUUID().toString();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category1));
        when(productRepository.findAllByCategoryId(categoryId)).thenReturn(false);

        // Act
        boolean result = categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(category1);
        verify(productRepository, times(1)).findAllByCategoryId(categoryId);

        assertThat(result).isTrue();
    }

    @Test
    void deleteCategory_shouldThrowException_whenCategoryIdNotExist(){

        // Arrange
        String categoryId = UUID.randomUUID().toString();
        String expectedErrorMessage = "Category not found for Id: %s".formatted(categoryId);

        when(productRepository.findAllByCategoryId(categoryId)).thenReturn(false);

        when(categoryRepository.findById(categoryId))
                .thenThrow(new ResourceNotFoundException("Category", "Id", categoryId));

        // Act
        Exception exception = assertThrows(ResourceNotFoundException.class,
                              () -> categoryService.deleteCategory(categoryId));

        // Assert
        verify(productRepository, times(1)).findAllByCategoryId(categoryId);
        verify(categoryRepository, times(1)).findById(categoryId);

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void deleteCategory_shouldThrowException_whenCategoryIdExist_andCategoryHasAssignedProducts(){

        // Arrange
        String categoryId = UUID.randomUUID().toString();

        when(productRepository.findAllByCategoryId(categoryId)).thenReturn(true);

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class,
                              () -> categoryService.deleteCategory(categoryId));

        // Assert
        verify(productRepository, times(1)).findAllByCategoryId(categoryId);
        verify(categoryRepository, times(0)).findById(categoryId);

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getMessage()).isEqualTo("Delete failed. Category has products assigned to it");
    }
}