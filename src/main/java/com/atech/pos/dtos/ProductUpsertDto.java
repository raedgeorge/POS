package com.atech.pos.dtos;

public record ProductUpsertDto(
        String id,
        String productName,
        String productPrice,
        String quantity,
        String categoryId
) {}
