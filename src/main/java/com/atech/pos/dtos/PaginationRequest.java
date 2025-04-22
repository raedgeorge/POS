package com.atech.pos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

@Data
@Builder
@AllArgsConstructor
public class PaginationRequest {

    private int pageNumber;
    private int pageSize;
    private String sortDirection;
    private String sortBy;
    private String filterText;

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final String ASCENDING = "ASC";
    private static final String DESCENDING = "DESC";

    public static PaginationRequest getPaginationRequest(Integer pageNumber,
                                                         Integer pageSize,
                                                         String sortDirection,
                                                         String sortBy,
                                                         String filterText){

        if (pageNumber == null || pageNumber < 0)
            pageNumber = DEFAULT_PAGE_NUMBER;

        if (pageSize == null || pageSize <= 0)
            pageSize = DEFAULT_PAGE_SIZE;

        if (sortDirection.equalsIgnoreCase(ASCENDING)) {
            sortDirection = Sort.Direction.ASC.name();
        } else {
            sortDirection = Sort.Direction.DESC.name();
        }

        return PaginationRequest.builder()
                .sortBy(sortBy)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .filterText(filterText)
                .sortDirection(sortDirection)
                .build();
    }

}
