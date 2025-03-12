package com.atech.pos.service;

import com.atech.pos.dtos.PagedProducts;
import com.atech.pos.dtos.PaginationRequest;
import com.atech.pos.dtos.ProductDto;
import com.atech.pos.dtos.ProductUpsertDto;

public interface ProductService {

    PagedProducts getAllProducts(String categoryId, PaginationRequest paginationRequest);

    ProductDto findProductById(String productId);

    ProductDto findProductByName(String productName);

    String createProduct(ProductUpsertDto productUpsertDto);

    ProductDto updateProduct(ProductUpsertDto productUpsertDto);

    boolean deleteProduct(String productId);
}
