package com.manager.email.adapter.out.email;

import com.manager.auth.adapter.config.email.ResendConfigurationProperties;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.model.Email;
import com.manager.email.model.EmailFailedException;
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
    public void sendEmail(Email email) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(configuration.fromEmail())
                .to(email.toEmail())
                .subject(email.subject())
                .html(email.body())
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("Email sent. Id: {}", data.getId());
        } catch (ResendException e) {
            log.error("Failed to send email via Resend", e);
            throw new EmailFailedException("Could not send email");
        }
    }

    @Override
    public String getSupportEmail() {
        return configuration.supportEmail();
    }
}
