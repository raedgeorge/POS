package com.atech.pos.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductUpsertDto {

        private String id;

        @NotBlank(message = "Product name is required")
        String productName;

        String description;

        String barCode;

        @Min(value = 1, message = "Product price should be greater than or equal to {value}")
        double productPrice;

        @Min(value = 1, message = "Selling price should be greater than or equal to {value}")
        double sellingPrice;

        @Min(value = 1, message = "Product quantity should be greater than or equal to {value}")
        int quantity;

        double discount;

        boolean isTaxable;

        BigDecimal taxRate;

        boolean isActive;

        @NotBlank(message = "Category Id is required")
        String categoryId;

        String supplier;

        public ProductUpsertDto(String productName,
                                double productPrice,
                                double sellingPrice,
                                int quantity,
                                String categoryId) {

                this.productName = productName;
                this.productPrice = productPrice;
                this.sellingPrice = sellingPrice;
                this.quantity = quantity;
                this.categoryId = categoryId;
        }

        public ProductUpsertDto(String id,
                                String productName,
                                double productPrice,
                                double sellingPrice,
                                int quantity,
                                String categoryId) {
                this.id = id;
                this.productName = productName;
                this.productPrice = productPrice;
                this.sellingPrice = sellingPrice;
                this.quantity = quantity;
                this.categoryId = categoryId;
        }
}
