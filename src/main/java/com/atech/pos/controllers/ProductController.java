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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.atech.pos.dtos.PaginationRequest.getPaginationRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;

    /**
     *
     * @param pageNumber pagination property. pageIndex
     * @param pageSize pagination property. pageSize
     * @param sortDirection pagination property. Sort asc | desc
     * @param sortBy pagination property. sort by field
     * @param filterText results filtering
     * @param categoryId categoryId
     * @return pagination result of Product entity
     */
    @GetMapping
    public ResponseEntity<PagedProducts> getProductsList(@RequestParam(required = false) Integer pageNumber,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) String sortDirection,
                                                         @RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false) String filterText,
                                                         @RequestParam(required = false) String categoryId){

        PaginationRequest paginationRequest = getPaginationRequest(
                pageNumber, pageSize, sortDirection, sortBy, filterText);

        return ResponseEntity.ok(productService.getAllProducts(paginationRequest, categoryId));
    }


    /**
     *
     * @param productId requested product ID
     * @return found ProductDto
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable @NotBlank(message = "Product Id is required") String productId){

        return ResponseEntity.ok(productService.findProductById(productId));
    }


    /**
     *
     * @param productUpsertDto request body used to create a new product
     * @param response add Location Header of created product ID
     * @return ID of created product
     */
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductUpsertDto productUpsertDto,
                                                HttpServletResponse response){

        String productId = productService.createProduct(productUpsertDto);

        response.setHeader(HttpHeaders.LOCATION, "/api/v1/products/%s".formatted(productId));

        return ResponseEntity.ok(productId);
    }


    /**
     *
     * @param productUpsertDto request body used to update a product
     * @return updated product Dto
     */
    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductUpsertDto productUpsertDto){

        return ResponseEntity.ok(productService.updateProduct(productUpsertDto));
    }


    /**
     *
     * @param productId to delete product ID
     * @return True if deletion was successful
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(
            @PathVariable @NotBlank(message = "Product Id is required") String productId){

        return ResponseEntity.ok(productService.deleteProduct(productId));
    }
}
