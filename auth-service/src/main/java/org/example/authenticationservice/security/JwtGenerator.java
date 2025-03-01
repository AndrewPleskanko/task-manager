package org.example.authenticationservice.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtGenerator {
    private static final int MIN_KEY_LENGTH = 64;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private Key getKey() {
        if (jwtSecretKey.getBytes(StandardCharsets.UTF_8).length < MIN_KEY_LENGTH) {
            log.error("The provided JWT secret key is not secure enough for HS512. "
                    + "Please provide a key that is at least " + MIN_KEY_LENGTH + " bytes (512 bits) long.");
            throw new IllegalArgumentException("Insecure JWT secret key");
        } else {
            log.info("Secret Key: {}", jwtSecretKey); //Додати логування
            return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

        Key key = getKey();

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        log.info("Generating token for user: {}", username);
        log.debug("New token: {}", token);
        return token;
    }

    public String getUsernameFromJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            log.info("Extracting username from JWT: {}", username);
            return username;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            log.info("Validating token: {}", token);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}