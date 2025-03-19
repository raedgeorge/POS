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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.atech.pos.security.service.AuthenticatedUserService.getAuthenticatedUser;
import static com.atech.pos.utils.EntityUtils.resolveSortByField;
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

        String sortBy = resolveSortByField(Product.class, paginationRequest.getSortBy(), "productName");

        Page<Product> pagedProducts = productRepository.findAll(PageRequest.of(
                paginationRequest.getPageNumber(),
                paginationRequest.getPageSize(),
                Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection()), sortBy)));

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

        checkIfProductExistThrowException(productUpsertDto.getProductName());

        checkIfCategoryNotExistThrowException(productUpsertDto.getCategoryId());

        Product product = productUpsertDtoMapper.mapToEntity(productUpsertDto);
        product.setProductName(convertEachWorldToFirstLetterUpperCase(product.getProductName()));
        product.setEnteredBy(getAuthenticatedUser());
        Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    @Override
    public ProductDto updateProduct(ProductUpsertDto productUpsertDto) {

        if (ObjectUtils.isEmpty(productUpsertDto.getId()))
            throw new IllegalArgumentException("Product Id field is required");

        if (!productRepository.existsById(productUpsertDto.getId()))
            throw new ResourceNotFoundException("Product", "Id", productUpsertDto.getId());

        checkIfCategoryNotExistThrowException(productUpsertDto.getCategoryId());

        checkIfProductToUpdateExistThrowException(productUpsertDto);

        return productRepository.findById(productUpsertDto.getId())
                .map(product -> {
                    populateProductFromRequestDto(productUpsertDto, product);

                    Product updatedProduct = productRepository.save(product);
                    return productMapper.mapToDto(updatedProduct);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productUpsertDto.getId()));
    }

    @Override
    public boolean deleteProduct(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));

        productRepository.delete(product);

        return true;
    }

    // *********************** Private methods *********************** \\
    private void checkIfProductExistThrowException(String productName) {

        productRepository.findByProductNameIgnoreCase(productName)
                .ifPresent(product -> {
                    throw new ResourceExistsException("Product", "Name", productName);
        });
    }

    private void checkIfProductToUpdateExistThrowException(ProductUpsertDto productUpsertDto) {

        productRepository.findByProductNameIgnoreCase(productUpsertDto.getProductName())
                .ifPresent(product -> {
                    if (!product.getId().equals(productUpsertDto.getId()))
                        throw new IllegalArgumentException(
                                "Product [%s] already exists".formatted(productUpsertDto.getProductName()));
                });
    }

    private void checkIfCategoryNotExistThrowException(String categoryId) {

        if (!categoryService.categoryExistsById(categoryId))
            throw new ResourceNotFoundException("Category", "Id", categoryId);
    }

    private static void populateProductFromRequestDto(ProductUpsertDto productUpsertDto, Product product) {

        BeanUtils.copyProperties(productUpsertDto, product);

        product.setEnteredBy(getAuthenticatedUser());
        product.setLastModified(LocalDateTime.now());
        product.setProductName(convertEachWorldToFirstLetterUpperCase(productUpsertDto.getProductName()));
    }

}
