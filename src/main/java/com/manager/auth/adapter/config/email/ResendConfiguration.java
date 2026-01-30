package com.manager.auth.adapter.config.email;

import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
@EnableConfigurationProperties(ResendConfigurationProperties.class)
public class ResendConfiguration {

    private final ResendConfigurationProperties configuration;

    @Bean
    public Resend resend() {
        return new Resend(configuration.apiKey());
    }
}
