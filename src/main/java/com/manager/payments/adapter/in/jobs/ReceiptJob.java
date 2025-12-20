package com.manager.payments.adapter.in.jobs;

import com.manager.payments.application.port.in.ProcessOverdueReceiptsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ReceiptJob {

    private final ProcessOverdueReceiptsUseCase processOverdueReceiptsUseCase;

    @Scheduled(cron = "${scheduled-jobs.receipts.cron}")
    public void updateOverdueReceipts() {
        LocalDate now = LocalDate.now();
        processOverdueReceiptsUseCase.processOverdueReceipts(now);
    }
}
