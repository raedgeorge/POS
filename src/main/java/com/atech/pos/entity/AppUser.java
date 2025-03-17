package com.atech.pos.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)

@Document(collection = "users")
public class AppUser extends BaseEntity{

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    private String username;

    private String password;

    private boolean expired;

    private boolean enabled;

    @Field(name = "lockout_end")
    private LocalDateTime lockoutEnd;

    @DBRef
    private Role role;

}
