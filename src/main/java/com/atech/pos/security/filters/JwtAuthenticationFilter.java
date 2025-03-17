package com.atech.pos.security.filters;

import com.atech.pos.entity.AppUser;
import com.atech.pos.repository.AppUserRepository;
import com.atech.pos.security.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final AppUserRepository appUserRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);
        final String username;
        final String token;

        if (!ObjectUtils.isEmpty(authHeader) && authHeader.startsWith("Bearer ")){

            token = authHeader.substring("Bearer ".length()).trim();
            username = jwtTokenService.extractUsername(token);

            Optional<AppUser> optionalAppUser = appUserRepository.findAppUserByUsername(username);

            if (optionalAppUser.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null){

                AppUser user = optionalAppUser.get();

                if (jwtTokenService.isTokenValid(token, user)){

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            user.getRole()
                                .getPermissions()
                                .stream()
                                .map(permission ->
                                    new SimpleGrantedAuthority("Permission_" + permission.name()))
                                .toList());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
