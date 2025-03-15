package com.atech.pos.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8160255205732581903L;

    private String id;
    private String categoryName;
    private String enteredBy;
    private String modifiedBy;
    private String creationDate;
    private String lastModified;

    public CategoryDto(String categoryName) {
        this.categoryName = categoryName;
    }
}
