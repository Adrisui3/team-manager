package com.manager.auth.adapter.config.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "spring.mail")
public record EmailConfigurationProperties(@NotBlank String host, @NotNull Integer port,
                                           @NotBlank @Email String username, @NotBlank String password,
                                           @NotEmpty Map<String, String> properties) {
}
