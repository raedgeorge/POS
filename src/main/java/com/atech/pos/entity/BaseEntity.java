package com.atech.pos.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class BaseEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    @Field(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Field(name = "last_modified")
    private LocalDateTime lastModified;
}
