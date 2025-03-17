package com.atech.pos.dtos;

import lombok.Data;

@Data
public class AppUserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String expired;
    private boolean enabled;
    private String lockoutEnd;
}
