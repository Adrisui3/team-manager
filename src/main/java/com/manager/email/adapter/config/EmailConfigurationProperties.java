package com.manager.email.adapter.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "email")
public record EmailConfigurationProperties(@NotNull Duration retention) {
}
