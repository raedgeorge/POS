package com.atech.pos.service.impl;

import com.atech.pos.dtos.LoginRequestDto;
import com.atech.pos.dtos.LoginResponseDto;
import com.atech.pos.entity.AppUser;
import com.atech.pos.exceptions.ResourceNotFoundException;
import com.atech.pos.repository.AppUserRepository;
import com.atech.pos.security.service.JwtTokenService;
import com.atech.pos.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenService jwtTokenService;
    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        Optional<AppUser> optionalAppUser = appUserRepository.findAppUserByUsername(loginRequestDto.username());

        if (optionalAppUser.isEmpty())
            throw new ResourceNotFoundException("User", "Username", loginRequestDto.username());

        try {

            AppUser user = optionalAppUser.get();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                           loginRequestDto.username(),
                                                           loginRequestDto.password()));

            loginResponseDto.setFullName(user.getFirstName() + " " + user.getLastName());

            loginResponseDto.setToken(jwtTokenService.generateToken(user));

        } catch (AuthenticationException exc){
            log.error("Authentication Failed: {}", exc.getMessage());
            throw new SecurityException(exc.getMessage());
        }

        log.info("Login Succeeded: {}", loginResponseDto);
        return loginResponseDto;
    }
}
