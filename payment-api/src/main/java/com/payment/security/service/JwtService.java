package com.payment.security.service;

import com.payment.exception.AuthenticationTokenNotValidException;
import com.payment.exception.TokenGenerationException;
import com.payment.model.AuthenticationToken;
import com.payment.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static java.util.Date.from;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private static final String ROLE = "role";

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public AuthenticationToken generateToken(User user) {
        final var expiresIn = from(Instant.now()
                .plus(1,
                        ChronoUnit.HOURS));

        return Optional.of(createToken(user, expiresIn))
                .map(token -> new AuthenticationToken(token, expiresIn))
                .orElseThrow(() -> {
                    throw new TokenGenerationException(String.format(
                            "Failed to generate token for user '%s'",
                            user.getUsername()
                    ));
                });
    }

    private String createToken(User user, Date expiresIn) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of(ROLE, user.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(expiresIn)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(User user, String token) {
        return user.getUsername()
                .equals(extractUsername(token))
                && notExpired(token);
    }

    private boolean notExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .after(new Date());
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new AuthenticationTokenNotValidException("Authentication token is expired or not valid.", e);
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
