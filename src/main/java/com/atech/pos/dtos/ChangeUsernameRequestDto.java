package com.atech.pos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUsernameRequestDto(

    @NotBlank(message = "User Id is required")
    String userId,

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 16, message = "Password should be between {min}-{max} characters")
    String newUsername

) {}
