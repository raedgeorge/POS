package com.atech.pos.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Document(collection = "products")
public class Product extends BaseEntity{

    @Field(name = "product_name")
    private String productName;

    @Field(name = "product_price")
    private double productPrice;

    private int quantity;

    private String categoryId;
}
