package com.atech.pos.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Document(collection = "categories")
public class Category extends BaseEntity {

    private String categoryName;
}
