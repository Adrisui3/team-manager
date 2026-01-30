package com.manager.auth.application.service.email;

import com.manager.auth.adapter.config.email.ResendConfigurationProperties;
import com.manager.auth.model.exceptions.EmailFailedException;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class ResendEmailService implements EmailService {

    private final Resend resend;
    private final ResendConfigurationProperties configuration;

    @Override
    public String getSupportEmail() {
        return configuration.supportEmail();
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(configuration.fromEmail())
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("Email sent. Id: {}", data.getId());
        } catch (ResendException e) {
            log.error("Failed to send email via Resend", e);
            throw new EmailFailedException("Could not send email");
        }
    }
}
