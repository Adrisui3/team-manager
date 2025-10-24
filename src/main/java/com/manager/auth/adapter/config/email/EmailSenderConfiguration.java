package com.manager.auth.adapter.config.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSenderConfiguration {

    private final EmailConfigurationProperties emailConfigurationProperties;

    public EmailSenderConfiguration(EmailConfigurationProperties emailConfigurationProperties) {
        this.emailConfigurationProperties = emailConfigurationProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfigurationProperties.host());
        mailSender.setPort(emailConfigurationProperties.port());
        mailSender.setUsername(emailConfigurationProperties.username());
        mailSender.setPassword(emailConfigurationProperties.password());

        Properties props = mailSender.getJavaMailProperties();
        for (String key : emailConfigurationProperties.properties().keySet()) {
            props.put(key, emailConfigurationProperties.properties().get(key));
        }

        return mailSender;
    }

}
