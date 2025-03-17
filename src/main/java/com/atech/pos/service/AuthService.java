package com.atech.pos.service;

import com.atech.pos.dtos.LoginRequestDto;
import com.atech.pos.dtos.LoginResponseDto;

public interface AuthService {

    LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto);
}
