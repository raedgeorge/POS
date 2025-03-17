package com.atech.pos.security.service;

import com.atech.pos.entity.AppUser;

public interface JwtTokenService {

    String generateToken(AppUser appUser);

    String extractUsername(String token);

    boolean isTokenValid(String token, AppUser appUser);
}
