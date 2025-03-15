package com.atech.pos.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2188341976008097534L;

    private String id;
    private String productName;
    private String description;
    private String barCode;
    private double productPrice;
    private double sellingPrice;
    private int quantity;
    private double discount;
    private boolean isTaxable;
    private BigDecimal taxRate;
    private boolean isActive;
    private String categoryId;
    private String categoryName;
    private String supplier;
    private String enteredBy;
    private String modifiedBy;
    private String creationDate;
    private String lastModified;
}
