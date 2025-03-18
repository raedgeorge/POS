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

    public static PaginationRequest getPaginationRequest(Integer pageNumber,
                                                         Integer pageSize,
                                                         String sortDirection,
                                                         String sortBy,
                                                         String filterText){

        if (pageNumber == null || pageNumber < 0)
            pageNumber = DEFAULT_PAGE_NUMBER;

        if (pageSize == null || pageSize <= 0)
            pageSize = DEFAULT_PAGE_SIZE;

        if (ObjectUtils.isEmpty(sortDirection) ||
            !(sortDirection.equalsIgnoreCase("ASC") || sortDirection.equalsIgnoreCase("DESC"))) {

            sortDirection = Sort.Direction.DESC.name();

        } else {
            sortDirection = sortDirection.toLowerCase();
        }

        return PaginationRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sortDirection(sortDirection)
                .sortBy(sortBy)
                .filterText(filterText)
                .build();
    }

}
