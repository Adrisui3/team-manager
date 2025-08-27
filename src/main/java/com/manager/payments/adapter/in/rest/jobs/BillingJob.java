package com.manager.payments.adapter.in.rest.jobs;

import com.manager.payments.application.port.in.IssueNewReceiptsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class BillingJob {

    private final IssueNewReceiptsUseCase issueNewReceiptsUseCase;

    public BillingJob(IssueNewReceiptsUseCase issueNewReceiptsUseCase) {
        this.issueNewReceiptsUseCase = issueNewReceiptsUseCase;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void billing() {
        LocalDate now = LocalDate.now();
        issueNewReceiptsUseCase.issueNewReceipts(now);
    }
}
