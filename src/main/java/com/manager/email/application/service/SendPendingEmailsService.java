package com.manager.email.application.service;

import com.manager.email.application.port.in.SendPendingEmailsUseCase;
import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendPendingEmailsService implements SendPendingEmailsUseCase {

    private final EmailRepository repository;
    private final TransactionalEmailSenderService senderService;

    @Override
    public void sendPendingEmails() {
        List<Email> pendingEmails = repository.findAllToBeSent();
        pendingEmails.forEach(senderService::sendEmail);
    }
}
