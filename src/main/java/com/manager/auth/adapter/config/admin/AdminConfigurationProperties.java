package com.manager.auth.adapter.config.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "admin")
public record AdminConfigurationProperties(@NotBlank @Email String email, @NotBlank String password) {
}
