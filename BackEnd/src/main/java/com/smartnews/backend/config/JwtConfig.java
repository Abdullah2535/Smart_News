package com.smartnews.backend.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;


@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
    private String secret;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public SecretKey getSecretKey() {
     // 1. Decodes the Base64 string into the correct raw bytes
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        // 2. Returns the secure key
        return Keys.hmacShaKeyFor(keyBytes);    }
}
