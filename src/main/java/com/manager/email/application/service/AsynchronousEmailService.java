package com.manager.email.application.service;

import com.manager.auth.adapter.config.email.ResendConfigurationProperties;
import com.manager.email.application.port.in.SendPendingEmailsUseCase;
import com.manager.email.application.port.in.SendVerificationEmailUseCase;
import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsynchronousEmailService implements SendPendingEmailsUseCase, SendVerificationEmailUseCase {

    private final EmailRepository repository;
    private final EmailTemplateService templateService;
    private final TransactionalEmailSenderService senderService;
    private final ResendConfigurationProperties configuration;

    @Override
    public void sendPendingEmails(LocalDateTime currentDate) {
        LocalDateTime targetDate = currentDate.minusDays(1);
        List<Email> pendingEmails = repository.findAllToBeSent(targetDate);
        pendingEmails.forEach(senderService::sendEmail);
    }

    @Override
    @Transactional
    public void sendVerificationEmail(String to, String verificationCode) {
        Email newEmail = Email.builder()
                .toEmail(to)
                .subject("Verification email")
                .body(templateService.loadVerificationEmailTemplate(verificationCode, configuration.supportEmail()))
                .status(EmailStatus.PENDING)
                .build();

        repository.save(newEmail);
    }
}
