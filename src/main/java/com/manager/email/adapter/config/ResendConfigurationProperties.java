package com.manager.email.adapter.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Validated
@Profile("prod")
@ConfigurationProperties(prefix = "email.resend")
public record ResendConfigurationProperties(@NotBlank String apiKey, @NotBlank String fromEmail,
                                            @NotBlank String supportEmail) {
}
