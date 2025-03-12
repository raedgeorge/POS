package com.atech.pos.dtos;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8160255205732581903L;

    private String id;
    private String categoryName;
    private String creationDate;
    private String lastModified;
}
