package com.atech.pos.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductUpsertDto(

        String id,

        @NotBlank(message = "Product name is required")
        String productName,

        @Min(value = 0, message = "Product price should be greater than {value}")
        String productPrice,

        @Min(value = 1, message = "Product quantity should be greater than {value}")
        String quantity,

        @NotBlank(message = "Category Id is required")
        String categoryId
) {}
