package com.atech.pos.controllers;

import com.atech.pos.dtos.LoginRequestDto;
import com.atech.pos.dtos.LoginResponseDto;
import com.atech.pos.dtos.RegistrationRequestDto;
import com.atech.pos.service.AppUserService;
import com.atech.pos.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService authService;
    private final AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){

        return ResponseEntity.ok(authService.authenticateUser(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequestDto registrationRequestDto){

        return ResponseEntity.ok(appUserService.createUser(registrationRequestDto));
    }
}
