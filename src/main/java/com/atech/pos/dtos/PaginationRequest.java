package com.atech.pos.dtos;

public record PaginationRequest (
        int pageNumber,
        int pageSize,
        String sortDirection,
        String sortBy,
        String filterText
){}
