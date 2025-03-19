package com.atech.pos.unit_tests.service;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.PagedProducts;
import com.atech.pos.dtos.PaginationRequest;
import com.atech.pos.dtos.ProductDto;
import com.atech.pos.entity.Product;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

        categoryDto = new CategoryDto("category 1");
        categoryDto.setId(categoryId);
    }

    @Test
    void getAllProducts() {

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

        when(productRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "productName"))))
                .thenReturn(page);

        // Act
        PagedProducts pagedProducts = productService.getAllProducts(paginationRequest, categoryId);

        // Assert
        verify(productRepository, times(1)).findAll(any(PageRequest.class));

        assertThat(pagedProducts.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    void findProductById() {
    }

    @Test
    void findProductByName() {
    }

    @Test
    void createProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}