package com.manager.email.application.service;

import com.manager.email.application.port.out.EmailRepository;
import com.manager.email.application.port.out.EmailService;
import com.manager.email.model.Email;
import com.manager.email.model.EmailFailedException;
import com.manager.email.model.EmailStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionalEmailSenderService {

    private final EmailRepository repository;
    private final EmailService service;

    public void sendEmail(Email email) {
        try {
            service.sendEmail(email);
            repository.save(email.toBuilder()
                    .status(EmailStatus.SENT)
                    .sentAt(LocalDateTime.now())
                    .build());
        } catch (EmailFailedException e) {
            log.error("Email {} failed to be sent twice. Marking it as DISCARDED.", email.id());
            repository.save(email.toBuilder()
                    .status(EmailStatus.DISCARDED)
                    .build());
        }
    }

}
