package com.manager.email.application.service;

import com.manager.email.application.port.in.SendExpiredReceiptEmailUseCase;
import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendExpiredReceiptEmailService implements SendExpiredReceiptEmailUseCase {

    private final EmailRepository repository;
    private final EmailTemplateService templateService;
    private final EmailService emailService;

    @Override
    @Transactional
    public void sendExpiredReceiptEmail(ExpiredReceiptEmailRequest request) {
        Email newEmail = Email.builder()
                .toEmail(request.playerEmail())
                .subject("Recibo vencido")
                .body(templateService.loadExpiredReceiptEmailTemplate(request, emailService.getSupportEmail()))
                .status(EmailStatus.PENDING)
                .build();

        repository.save(newEmail);
    }
}
