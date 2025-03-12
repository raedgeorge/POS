package com.atech.pos.dtos;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2188341976008097534L;

    private String id;
    private String productName;
    private String productPrice;
    private String quantity;
    private String creationDate;
    private String lastModified;
    private String categoryId;
    private String categoryName;
}
