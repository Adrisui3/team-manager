package com.manager.email.adapter.config;

import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class ResendConfiguration {

    private final ResendConfigurationProperties configuration;

    @Bean
    public Resend resend() {
        return new Resend(configuration.apiKey());
    }
}
