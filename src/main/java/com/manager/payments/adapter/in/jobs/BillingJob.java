package com.manager.payments.adapter.in.jobs;

import com.manager.payments.application.port.in.IssueNewReceiptsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BillingJob {

    private final IssueNewReceiptsUseCase issueNewReceiptsUseCase;

    @Scheduled(cron = "${scheduled-jobs.billing.cron}")
    public void billing() {
        LocalDate now = LocalDate.now();
        issueNewReceiptsUseCase.issueNewReceipts(now);
    }
}
