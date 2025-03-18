package com.atech.pos.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String expired;
    private boolean enabled;
    private String lockoutEnd;
}
