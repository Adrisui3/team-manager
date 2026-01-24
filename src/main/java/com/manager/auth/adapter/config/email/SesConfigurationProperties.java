package com.manager.auth.adapter.config.email;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Validated
@Profile("prod")
@ConfigurationProperties(prefix = "email.ses")
public record SesConfigurationProperties(@NotBlank String region, @NotBlank String fromEmail) {
}
