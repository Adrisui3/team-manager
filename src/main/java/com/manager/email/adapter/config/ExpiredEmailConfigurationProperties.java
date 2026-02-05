package com.manager.email.adapter.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "scheduled-jobs.expired-emails")
public record ExpiredEmailConfigurationProperties(@NotNull Duration retention) {
}
