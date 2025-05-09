package org.example.taskservice.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final int MIN_KEY_LENGTH = 64;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private Key getKey() {
        if (jwtSecretKey.getBytes(StandardCharsets.UTF_8).length < MIN_KEY_LENGTH) {
            log.error("The provided JWT secret key is not secure enough for HS512. "
                    + "Please provide a key that is at least " + MIN_KEY_LENGTH + " bytes (512 bits) long.");
            throw new IllegalArgumentException("Insecure JWT secret key");
        } else {
            log.info("Secret Key: {}", jwtSecretKey);
            return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Long userId = claims.get("userId", Long.class);
            log.debug("Extracted userId from JWT: {}", userId);
            return userId;
        }
        return null;
    }
}
