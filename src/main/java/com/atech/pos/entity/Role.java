package com.atech.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Document(collection = "roles")
public class Role extends BaseEntity {

    @Field(name = "role_type")
    private RoleType roleType;

    private List<Permissions> permissions;
}
