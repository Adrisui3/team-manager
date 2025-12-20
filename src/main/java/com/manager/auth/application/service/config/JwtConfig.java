package com.manager.auth.application.service.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfig(@NotBlank String secretKey, long expirationTime) {
}
