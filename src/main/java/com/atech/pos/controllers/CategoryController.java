package com.atech.pos.controllers;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    /**
     *
     * @return List of all CategoryDtos
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesList(){

        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    /**
     *
     * @param categoryId required category
     * @return CategoryDto
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable @NotBlank(message = "Category Id is required") String categoryId){

        return ResponseEntity.ok(categoryService.findCategoryById(categoryId));
    }


    /**
     *
     * @param categoryName required category
     * @return CategoryDto
     */
    @GetMapping("/name")
    public ResponseEntity<CategoryDto> getCategoryByName(
            @RequestParam @NotBlank(message = "Category name is required") String categoryName){

        return ResponseEntity.ok(categoryService.findCategoryByName(categoryName));
    }


    /**
     *
     * @param categoryUpsertDto RequestBody. CategoryUpsertDto to create
     * @param response created category ID
     * @return created category ID
     */
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryUpsertDto categoryUpsertDto,
                                                 HttpServletResponse response){

        String categoryId = categoryService.createCategory(categoryUpsertDto);

        response.addHeader(HttpHeaders.LOCATION, "/api/v1/categories/%s".formatted(categoryId));

        return ResponseEntity.ok(categoryId);
    }


    /**
     *
     * @param categoryUpsertDto RequestBody. CategoryUpsertDto to update
     * @return updated CategoryDto
     */
    @PutMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryUpsertDto categoryUpsertDto){

        return ResponseEntity.ok(categoryService.updateCategory(categoryUpsertDto));
    }


    /**
     *
     * @param categoryId of Category to delete
     * @return True if delete was successful
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable @NotBlank String categoryId){

        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }
}
