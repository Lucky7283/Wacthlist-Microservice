package com.example.watchlist.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtService {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration ttl;
    private final String issuer;

    public JwtService(@Value("${app.jwt.signing-key}") String secret, @Value("${app.jwt.ttl}") Duration ttl,
            @Value("${app.jwt.issuer:Watchlist Service}") String issuer) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32 || ttl.isNegative() || ttl.isZero() || issuer == null || issuer.isBlank())
            throw new IllegalArgumentException("JWT configuration is invalid");
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        encoder = NimbusJwtEncoder.withSecretKey(key).algorithm(MacAlgorithm.HS256).build();
        var d = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        d.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        decoder = d;
        this.ttl = ttl;
        this.issuer = issuer;
    }

    public String issue(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        Instant now = Instant.now();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),
                JwtClaimsSet.builder().issuer(issuer).subject(userId.toString()).issuedAt(now)
                        .expiresAt(now.plus(ttl)).build()))
                .getTokenValue();
    }

    public Long userId(String token) {
        var jwt = decoder.decode(token);
        if (jwt.getExpiresAt() == null) {
            throw new org.springframework.security.oauth2.jwt.JwtException("Token expiration is required");
        }
        Long id = Long.valueOf(jwt.getSubject());
        if (id <= 0) {
            throw new IllegalArgumentException("Token subject must be a positive user ID");
        }
        return id;
    }

    public boolean matches(String token) {
        try {
            userId(token);
            return true;
        } catch (org.springframework.security.oauth2.jwt.JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean matches(String token, Long expectedUserId) {
        if (expectedUserId == null || expectedUserId <= 0) {
            return false;
        }
        try {
            return expectedUserId.equals(userId(token));
        } catch (org.springframework.security.oauth2.jwt.JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public Duration ttl() {
        return ttl;
    }

}
