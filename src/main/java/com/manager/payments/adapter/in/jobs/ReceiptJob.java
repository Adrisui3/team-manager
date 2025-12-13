package com.manager.payments.adapter.in.jobs;

import com.manager.payments.application.port.in.ProcessOverdueReceiptsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReceiptJob {

    private final ProcessOverdueReceiptsUseCase processOverdueReceiptsUseCase;

    public ReceiptJob(ProcessOverdueReceiptsUseCase processOverdueReceiptsUseCase) {
        this.processOverdueReceiptsUseCase = processOverdueReceiptsUseCase;
    }

    @Scheduled(cron = "${scheduled-jobs.receipts.cron}")
    public void updateOverdueReceipts() {
        LocalDate now = LocalDate.now();
        processOverdueReceiptsUseCase.processOverdueReceipts(now);
    }
}
