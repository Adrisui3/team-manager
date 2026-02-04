package com.manager.email.adapter.out.email;

import com.manager.email.application.port.out.EmailService;
import com.manager.email.model.Email;
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
    public void sendEmail(Email email) {
        log.info("Sending email to {} with subject {} and content {}", email.toEmail(), email.subject(), email.body());
    }
}
