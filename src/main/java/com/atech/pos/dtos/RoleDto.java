package com.atech.pos.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {

    private String id;
    private String name;
    private List<String> permissions;
}
