package com.project.SupportFlow.config;

import com.project.SupportFlow.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.expiration}")
    private Long tokenExpiration;
    @Value("${jwt.secret}")
    private String tokenSecret;

    public String gerarToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return buildToken(user);
    }

    private String buildToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + tokenExpiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey())
                .compact();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(tokenSecret.getBytes());
    }

    public boolean validateToken(String token) {
        try{
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
