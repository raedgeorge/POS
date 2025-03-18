package com.atech.pos.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedUsers extends PageImpl<AppUserDto> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagedUsers(@JsonProperty("list") List<AppUserDto> users,
                      @JsonProperty("pageNumber") int pageNumber,
                      @JsonProperty("pageSize") int pageSize,
                      @JsonProperty("numberOfElements") int numberOfElements,
                      @JsonProperty("totalElements") long totalElements,
                      @JsonProperty("totalPages") int totalPages) {

        super(users, PageRequest.of(pageNumber, pageSize), totalElements);
    }
}
