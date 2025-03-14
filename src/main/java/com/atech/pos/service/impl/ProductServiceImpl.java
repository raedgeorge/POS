package com.atech.pos.service.impl;

import com.atech.pos.dtos.PagedProducts;
import com.atech.pos.dtos.PaginationRequest;
import com.atech.pos.dtos.ProductDto;
import com.atech.pos.dtos.ProductUpsertDto;
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

import java.lang.reflect.Field;
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

        List<ProductDto> productDtos = pagedProducts.getContent().stream().map(product -> {

            ProductDto productDto = productMapper.mapToDto(product);

            productDto.setCategoryName(categoryService.findCategoryById(
                                       product.getCategoryId()).getCategoryName());

            return productDto;
        }).toList();

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
        return productRepository.findById(productId).map(product -> {

            ProductDto productDto = productMapper.mapToDto(product);

            productDto.setCategoryName(categoryService.findCategoryById(
                                       product.getCategoryId()).getCategoryName());

            return productDto;
        }).orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
    }

    @Override
    public ProductDto findProductByName(String productName) {
        return null;
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
        return null;
    }

    @Override
    public boolean deleteProduct(String productId) {
        return false;
    }

    // *********************** Private methods *********************** \\
    private void checkIfProductExistThrowException(ProductUpsertDto productUpsertDto) {

        productRepository.findByProductNameIgnoreCase(
                productUpsertDto.productName()).ifPresent(product -> {
                    throw new ResourceExistsException("Product", "Name", productUpsertDto.productName());
        });
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
