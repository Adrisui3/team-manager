package com.manager.email.application.service;

import com.manager.email.application.port.in.DeleteExpiredEmailsUseCase;
import com.manager.email.application.port.out.EmailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteExpiredEmailsService implements DeleteExpiredEmailsUseCase {

    private final EmailRepository repository;

    @Override
    @Transactional
    public void deleteExpiredEmails(LocalDateTime targetDate) {
        int deleted = repository.deleteExpired(targetDate);
        if (deleted > 0) {
            log.info("Deleting {} expired emails older than {}", deleted, targetDate);
        }
    }
}
