package com.atech.pos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpsertDto(
        String id,

        @NotBlank(message = "CategoryName field is required")
        @Size(max = 32, message = "Category name should be less than or equal to {max} characters")
        String categoryName
) {}
