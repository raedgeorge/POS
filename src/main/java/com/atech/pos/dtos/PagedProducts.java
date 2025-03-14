package com.atech.pos.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PagedProducts extends PageImpl<ProductDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6522748174283777591L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagedProducts(@JsonProperty("products") List<ProductDto> products,
                         @JsonProperty("pageNumber") int pageNumber,
                         @JsonProperty("pageSize") int pageSize,
                         @JsonProperty("numberOfElements") int numberOfElements,
                         @JsonProperty("totalElements") long totalElements,
                         @JsonProperty("totalPages") int totalPages) {
        super(products, PageRequest.of(pageNumber, pageSize), totalElements);
    }

    public PagedProducts(List<ProductDto> content) {
        super(content);
    }
}
