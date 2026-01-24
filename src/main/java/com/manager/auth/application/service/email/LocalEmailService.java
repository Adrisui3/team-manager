package com.manager.auth.application.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!prod")
@RequiredArgsConstructor
@Slf4j
public class LocalEmailService implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String text) {
        log.info("Sending email to {} with subject {} and content {}", to, subject, text);
    }
}
