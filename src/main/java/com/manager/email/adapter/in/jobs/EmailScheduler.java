package com.manager.email.adapter.in.jobs;

import com.manager.email.adapter.config.EmailConfigurationProperties;
import com.manager.email.application.port.in.DeleteExpiredEmailsUseCase;
import com.manager.email.application.port.in.SendPendingEmailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailScheduler {

    private final SendPendingEmailsUseCase sendPendingEmailsUseCase;
    private final DeleteExpiredEmailsUseCase deleteExpiredEmailsUseCase;
    private final EmailConfigurationProperties emailConfiguration;

    @Scheduled(cron = "${scheduled-jobs.email.cron}")
    public void sendPendingEmails() {
        sendPendingEmailsUseCase.sendPendingEmails();
    }

    @Scheduled(cron = "${scheduled-jobs.expired-emails.cron}")
    public void deleteExpiredEmails() {
        deleteExpiredEmailsUseCase.deleteExpiredEmails(LocalDateTime.now().minus(emailConfiguration.retention()));
    }
}
