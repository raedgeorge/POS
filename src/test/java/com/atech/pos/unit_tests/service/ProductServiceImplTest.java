package com.atech.pos.unit_tests.service;

import com.atech.pos.dtos.*;
import com.atech.pos.entity.Product;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.mappers.ProductMapper;
import com.atech.pos.mappers.ProductUpsertDtoMapper;
import com.atech.pos.repository.ProductRepository;
import com.atech.pos.service.CategoryService;
import com.atech.pos.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductUpsertDtoMapper productUpsertDtoMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    Product product1;
    Product product2;
    List<Product> productList;

    ProductDto productDto1;
    ProductDto productDto2;
    List<ProductDto> productDtoList;

    CategoryDto categoryDto;

    String categoryId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {

        product1 = new Product(
                "apples", 2.99, 4.99, 100, categoryId);

        product2 = new Product(
                "oranges", 1.99, 3.99, 200, categoryId);

        productDto1 = new ProductDto(
                "apples", 2.99, 4.99, 100, categoryId);

        productDto2 = new ProductDto(
                "oranges", 1.99, 3.99, 200, categoryId);

        productList = List.of(product1, product2);
        productDtoList = List.of(productDto1, productDto2);

        categoryDto = new CategoryDto(categoryId,"category 1");
    }

    @Test
    void getAllProducts_shouldReturnPagedProducts() {

        // Arrange
        categoryDto.setId(UUID.randomUUID().toString());

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .pageNumber(0)
                .pageSize(10)
                .sortDirection("asc")
                .sortBy("")
                .filterText("")
                .build();

        when(productMapper.mapToDto(product1)).thenReturn(productDto1);
        when(productMapper.mapToDto(product2)).thenReturn(productDto2);
        when(categoryService.findCategoryById(categoryId)).thenReturn(categoryDto);

        Page<Product> page = new PageImpl<>(productList, PageRequest.of(0, 10), productList.size());

        when(productRepository.findAll(PageRequest
                    .of(0, 10, Sort.by(Sort.Direction.ASC, "productName"))))
                .thenReturn(page);

        // Act
        PagedProducts pagedProducts = productService.getAllProducts(paginationRequest, categoryId);

        // Assert
        verify(productRepository, times(1)).findAll(any(PageRequest.class));

        assertThat(pagedProducts.getTotalPages()).isEqualTo(1);
        assertThat(pagedProducts.getNumberOfElements()).isEqualTo(2);
        assertThat(pagedProducts.getContent().get(0)).isEqualTo(productDto1);
    }

    @Test
    void findProductById_shouldReturnProduct_whenProductIdExists() {

        // Arrange
        String productId = product1.getId();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));
        when(productMapper.mapToDto(product1)).thenReturn(productDto1);
        when(categoryService.findCategoryById(categoryId)).thenReturn(categoryDto);

        // Act
        ProductDto productDto = productService.findProductById(productId);

        // Assert
        verify(productRepository, times(1)).findById(productId);
        verify(categoryService, times(1)).findCategoryById(categoryId);

        assertThat(productDto).isNotNull();
        assertThat(productDto.getProductName()).isEqualTo(product1.getProductName());
        assertThat(productDto.getCategoryName()).isEqualTo(categoryDto.getCategoryName());
    }

    @Test
    void findProductById_shouldThrowException_whenProductIdNotExists() {

        // Arrange
        String productId = UUID.randomUUID().toString();
        when(productRepository.findById(productId))
                .thenThrow(new ResourceNotFoundException("Product", "Id", productId));

        // Act
        Exception exception = assertThrows(ResourceNotFoundException.class,
                                () -> productService.findProductById(productId));

        // Assert
        verify(productMapper, times(0)).mapToDto(product1);
        verify(productRepository, times(1)).findById(productId);

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("Product not found for Id: %s".formatted(productId));
    }

    @Test
    void createProduct_shouldCreate_whenProductNotExist() {

        // Arrange
        ProductUpsertDto productUpsertDto = new ProductUpsertDto(
                "apples and oranges", 1.99, 2.99, 20, categoryDto.getId());

        when(productRepository.findByProductNameIgnoreCase(productUpsertDto.getProductName()))
                .thenReturn(Optional.empty());

        when(categoryService.categoryExistsById(categoryDto.getId())).thenReturn(true);

        when(productUpsertDtoMapper.mapToEntity(productUpsertDto)).thenReturn(product1);

        when(productRepository.save(product1)).thenReturn(product1);

        // Act
        String productId = productService.createProduct(productUpsertDto);

        // Assert
        verify(productRepository, times(1))
                .findByProductNameIgnoreCase(productUpsertDto.getProductName());

        verify(productRepository, times(1)).save(product1);
        verify(productUpsertDtoMapper, times(1)).mapToEntity(productUpsertDto);
        verify(categoryService, times(1)).categoryExistsById(categoryDto.getId());

        assertThat(productId).isNotNull();
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct_shouldDelete_whenProductIdExist() {

        // Arrange
        String productId = UUID.randomUUID().toString();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));

        // Act
        boolean result = productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).findById(productId);

        assertThat(result).isTrue();
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductIdNotExist(){

        // Arrange
        String productId = UUID.randomUUID().toString();
        when(productRepository.findById(productId))
                .thenThrow(new ResourceNotFoundException("Product", "Id", productId));

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                () -> productService.deleteProduct(productId));

        // Assert
        verify(productRepository, times(1)).findById(productId);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Product not found for Id: %s".formatted(productId));
    }
}