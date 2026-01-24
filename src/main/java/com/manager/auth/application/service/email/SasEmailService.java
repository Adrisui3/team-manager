package com.manager.auth.application.service.email;

import com.manager.auth.adapter.config.email.SesConfigurationProperties;
import com.manager.auth.model.exceptions.VerificationEmailFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class SasEmailService implements EmailService {

    private final SesClient sesClient;
    private final SesConfigurationProperties configuration;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(configuration.fromEmail())
                    .destination(Destination.builder()
                            .toAddresses(to)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data(subject)
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .html(Content.builder()
                                            .data(body)
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("Email sent via SES. MessageId: {}", response.messageId());
        } catch (Exception e) {
            log.error("Failed to send email via SES", e);
            throw new VerificationEmailFailedException("Could not send email");
        }
    }
}
