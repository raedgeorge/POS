package com.atech.pos.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserService {

    public static String getAuthenticatedUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){
            return (String) authentication.getPrincipal();
        }

        return "";
    }
}
