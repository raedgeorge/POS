package com.atech.pos.controllers;

import com.atech.pos.dtos.PagedProducts;
import com.atech.pos.dtos.PaginationRequest;
import com.atech.pos.dtos.ProductDto;
import com.atech.pos.dtos.ProductUpsertDto;
import com.atech.pos.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PagedProducts> getProductsList(@RequestParam(required = false) Integer pageNumber,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) String sortDirection,
                                                         @RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false) String filterText,
                                                         @RequestParam(required = false) String categoryId){

        if (pageNumber == null || pageNumber < 0)
            pageNumber = 0;

        if (pageSize == null || pageSize <= 0)
            pageSize = 10;

        if (ObjectUtils.isEmpty(sortDirection) ||
           (!sortDirection.equals("asc") && !sortDirection.equals("desc"))){

            sortDirection = Sort.Direction.DESC.name();
        }

        PaginationRequest paginationRequest = new PaginationRequest(
                pageNumber, pageSize, sortDirection, sortBy, filterText);

        return ResponseEntity.ok(productService.getAllProducts(paginationRequest, categoryId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable @NotBlank(message = "Product Id is required") String productId){

        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductUpsertDto productUpsertDto,
                                                HttpServletResponse response){

        String productId = productService.createProduct(productUpsertDto);

        response.setHeader(HttpHeaders.LOCATION, "/api/v1/products/%s".formatted(productId));

        return ResponseEntity.ok(productId);
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductUpsertDto productUpsertDto){

        return ResponseEntity.ok(productService.updateProduct(productUpsertDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(
            @PathVariable @NotBlank(message = "Product Id is required") String productId){

        return ResponseEntity.ok(productService.deleteProduct(productId));
    }
}
