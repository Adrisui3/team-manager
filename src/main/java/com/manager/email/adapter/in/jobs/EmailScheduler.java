package com.manager.email.adapter.in.jobs;

import com.manager.email.application.port.in.SendPendingEmailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailScheduler {

    private final SendPendingEmailsUseCase sendPendingEmailsUseCase;

    @Scheduled(cron = "${scheduled-jobs.email.cron}")
    public void sendPendingEmails() {
        sendPendingEmailsUseCase.sendPendingEmails(LocalDateTime.now());
    }
}
