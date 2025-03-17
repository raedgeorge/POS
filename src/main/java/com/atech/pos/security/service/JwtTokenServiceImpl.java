package com.atech.pos.security.service;

import com.atech.pos.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenServiceImpl implements JwtTokenService{

    @Value("${security.secret_key}")
    private String secretKey;

    @Value("${security.token_expiry}")
    private Long tokenExpiry;

    @Override
    public String generateToken(AppUser appUser) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", appUser.getFirstName() + " " + appUser.getLastName());
        claims.put("role", appUser.getRole().getRoleType().name());
        claims.put("permissions", appUser.getRole().getPermissions());

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(appUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiry))
                .setIssuer("pos@atech.com")
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, AppUser appUser) {

        final String username = extractUsername(token);

        return appUser.getUsername().equals(username) &&  !isTokenExpired(token);
    }

    private Key getSigninKey(){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){

        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date extractTokenExpiryDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractTokenExpiryDate(token).before(new Date());
    }
}
