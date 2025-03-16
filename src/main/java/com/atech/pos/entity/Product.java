package com.atech.pos.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Document(collection = "products")
public class Product extends BaseEntity{

    @Field(name = "product_name")
    private String productName;

    private String description;

    @Field(name = "bar_code")
    private String barCode;

    @Field(name = "product_price")
    private double productPrice;

    @Field(name = "selling_price")
    private double sellingPrice;

    private int quantity;

    private double discount;

    @Field(name = "is_taxable")
    private boolean taxable;

    @Field(name = "tax_rate")
    private BigDecimal taxRate;

    @Field(name = "is_active")
    private boolean active;

    @Field(name = "category_id")
    private String categoryId;

    private String supplier;
}
