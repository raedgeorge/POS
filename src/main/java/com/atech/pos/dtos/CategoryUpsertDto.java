package com.atech.pos.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpsertDto(
        String id,

        @NotBlank(message = "CategoryName field is required")
        String categoryName
) {}
