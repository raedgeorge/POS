package com.atech.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Document(collection = "roles")
public class Role extends BaseEntity {

    private String name;

    private List<Permissions> permissions;
}
