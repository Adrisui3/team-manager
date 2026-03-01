package com.manager.email.application.service;

import com.manager.email.application.port.in.SendVerificationEmailUseCase;
import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendVerificationEmailService implements SendVerificationEmailUseCase {

    private final EmailRepository repository;
    private final EmailTemplateService templateService;
    private final EmailService emailService;

    @Override
    @Transactional
    public void sendVerificationEmail(String to, String verificationCode) {
        Email newEmail = Email.builder()
                .toEmail(to)
                .subject("Verification email")
                .body(templateService.loadVerificationEmailTemplate(verificationCode, emailService.getSupportEmail()))
                .status(EmailStatus.PENDING)
                .build();

        repository.save(newEmail);
    }
}
