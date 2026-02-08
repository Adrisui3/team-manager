package com.manager.email.application.service;

import com.manager.email.application.port.in.DeleteExpiredEmailsUseCase;
import com.manager.email.application.port.in.SendPendingEmailsUseCase;
import com.manager.email.application.port.in.SendVerificationEmailUseCase;
import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsynchronousEmailService implements SendPendingEmailsUseCase, SendVerificationEmailUseCase,
        DeleteExpiredEmailsUseCase {

    private final EmailRepository repository;
    private final EmailTemplateService templateService;
    private final TransactionalEmailSenderService senderService;
    private final EmailService emailService;

    @Override
    public void sendPendingEmails() {
        List<Email> pendingEmails = repository.findAllToBeSent();
        pendingEmails.forEach(senderService::sendEmail);
    }

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

    @Override
    @Transactional
    public void deleteExpiredEmails(LocalDateTime targetDate) {
        int deleted = repository.deleteExpired(targetDate);
        if (deleted > 0) {
            log.info("Deleting {} expired emails older than {}", deleted, targetDate);
        }
    }
}
