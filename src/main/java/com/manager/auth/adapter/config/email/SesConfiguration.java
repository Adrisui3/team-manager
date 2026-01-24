package com.manager.auth.adapter.config.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class SesConfiguration {

    private final SesConfigurationProperties configuration;

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(configuration.region()))
                .build();
    }
}
