package com.atech.pos.service.impl;

import com.atech.pos.dtos.AppUserDto;
import com.atech.pos.dtos.LoginRequestDto;
import com.atech.pos.dtos.LoginResponseDto;
import com.atech.pos.service.AppUserService;
import com.atech.pos.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        AppUserDto user = appUserService.findUserByUsername(loginRequestDto.username());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                           loginRequestDto.username(),
                                                           loginRequestDto.password()));

            loginResponseDto.setFullName(user.getFirstName() + " " + user.getLastName());

            // TODO generate access token
            loginResponseDto.setToken("access-token");

        } catch (AuthenticationException exc){
            log.error("Authentication Failed: {}", exc.getMessage());
            throw new SecurityException(exc.getMessage());
        }

        log.info("Login Succeeded: {}", loginResponseDto);
        return loginResponseDto;
    }
}
