package com.atech.pos.controllers;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable @NotBlank(message = "Category Id is required") String categoryId){

        return ResponseEntity.ok(categoryService.findCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<CategoryDto> getCategoryByName(
            @RequestParam @NotBlank(message = "Category name is required") String categoryName){

        return ResponseEntity.ok(categoryService.findCategoryByName(categoryName));
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryUpsertDto categoryUpsertDto){

        String categoryId = categoryService.createCategory(categoryUpsertDto);

        return ResponseEntity.ok(categoryId);
    }
}
