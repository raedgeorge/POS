package com.atech.pos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(

   @NotBlank(message = "First name is required")
   String firstName,

   @NotBlank(message = "Last name is required")
   String lastName,

   @NotBlank(message = "Username is required")
   @Size(min = 4, max = 16, message = "User name should be between {min}-{max} characters")
   String username,

   @NotBlank(message = "Password is required")
   @Size(min = 8, max = 16, message = "Password should be between {min}-{max} characters")
   String password

) {}
