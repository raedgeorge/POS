package com.atech.pos.service.impl;

import com.atech.pos.dtos.*;
import com.atech.pos.entity.Product;
import com.atech.pos.exceptions.ResourceExistsException;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.mappers.ProductMapper;
import com.atech.pos.mappers.ProductUpsertDtoMapper;
import com.atech.pos.repository.ProductRepository;
import com.atech.pos.service.CategoryService;
import com.atech.pos.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.atech.pos.utils.StringUtils.convertEachWorldToFirstLetterUpperCase;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductUpsertDtoMapper productUpsertDtoMapper;

    @Override
    @Transactional
    public PagedProducts getAllProducts(PaginationRequest paginationRequest, String categoryId) {

        String sortBy = getOrderByField(paginationRequest.sortBy());

        Page<Product> pagedProducts = productRepository.findAll(PageRequest.of(
                paginationRequest.pageNumber(),
                paginationRequest.pageSize(),
                Sort.by(Sort.Direction.fromString(paginationRequest.sortDirection()), sortBy)));

        List<ProductDto> productDtos = pagedProducts.getContent()
                .stream()
                .map(productMapper::mapToDto)
                .peek(productDto -> productDto.setCategoryName(
                        categoryService.findCategoryById(productDto.getCategoryId()).getCategoryName()))
                .toList();

        return new PagedProducts(
                productDtos,
                pagedProducts.getPageable().getPageNumber(),
                pagedProducts.getPageable().getPageSize(),
                pagedProducts.getNumberOfElements(),
                pagedProducts.getTotalElements(),
                pagedProducts.getTotalPages()
                );
    }

    @Override
    public ProductDto findProductById(String productId) {

        return productRepository.findById(productId)
                .map(productMapper::mapToDto)
                .map(productDto -> {
                    CategoryDto category = categoryService.findCategoryById(productDto.getCategoryId());
                    productDto.setCategoryName(category.getCategoryName());
                    return productDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
    }

    @Override
    public ProductDto findProductByName(String productName) {

        return productRepository.findByProductNameIgnoreCase(productName)
                .map(productMapper::mapToDto)
                .map(productDto -> {
                    CategoryDto category = categoryService.findCategoryById(productDto.getCategoryId());
                    productDto.setCategoryName(category.getCategoryName());
                    return productDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Name", productName));
    }

    @Override
    public String createProduct(ProductUpsertDto productUpsertDto) {

        checkIfProductExistThrowException(productUpsertDto);

        Product product = productUpsertDtoMapper.mapToEntity(productUpsertDto);
        product.setProductName(convertEachWorldToFirstLetterUpperCase(product.getProductName()));
        Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    @Override
    public ProductDto updateProduct(ProductUpsertDto productUpsertDto) {

        if (ObjectUtils.isEmpty(productUpsertDto.id()))
            throw new IllegalArgumentException("Product Id field is required");

        if (!productRepository.existsById(productUpsertDto.id()))
            throw new ResourceNotFoundException("Product", "Id", productUpsertDto.id());

        checkIfProductToUpdateExistThrowException(productUpsertDto);

        return productRepository.findById(productUpsertDto.id())
                .map(product -> {
                    populateProductFromRequestDto(productUpsertDto, product);

                    Product updatedProduct = productRepository.save(product);
                    return productMapper.mapToDto(updatedProduct);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productUpsertDto.id()));
    }

    @Override
    public boolean deleteProduct(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));

        productRepository.delete(product);

        return true;
    }

    // *********************** Private methods *********************** \\
    private void checkIfProductExistThrowException(ProductUpsertDto productUpsertDto) {

        productRepository.findByProductNameIgnoreCase(
                productUpsertDto.productName()).ifPresent(product -> {
                    throw new ResourceExistsException("Product", "Name", productUpsertDto.productName());
        });
    }

    private void checkIfProductToUpdateExistThrowException(ProductUpsertDto productUpsertDto) {

        productRepository.findByProductNameIgnoreCase(productUpsertDto.productName())
                .ifPresent(product -> {
                    if (!product.getId().equals(productUpsertDto.id()))
                        throw new IllegalArgumentException(
                                "Product [%s] already exists".formatted(productUpsertDto.productName()));
                });
    }

    private static void populateProductFromRequestDto(ProductUpsertDto productUpsertDto, Product product) {

        product.setLastModified(LocalDateTime.now());
        product.setQuantity(productUpsertDto.quantity());
        product.setProductPrice(productUpsertDto.productPrice());
        product.setProductName(convertEachWorldToFirstLetterUpperCase(productUpsertDto.productName()));
    }

    private static String getOrderByField(String sortBy) {

        String defaultSortByField = "productName";

        Field[] fields = Product.class.getDeclaredFields();

        List<String> fieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        return fieldNames.contains(sortBy) ? sortBy : defaultSortByField;
    }
}
