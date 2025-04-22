package com.atech.pos.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RoleUpsertDto(
        String id,

        @JsonProperty("role_name")
        @NotBlank(message = "Role name field is required")
        String roleName,

        @Size(min = 1, message = "List should contain at least one value")
        List<String> permissions
) {}
