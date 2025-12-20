package com.manager.payments.adapter.in.jobs;

import com.manager.payments.application.port.in.ProcessExpiredPaymentsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PaymentJob {

    private final ProcessExpiredPaymentsUseCase processExpiredPaymentsUseCase;

    @Scheduled(cron = "${scheduled-jobs.payments.cron}")
    public void updateExpiredPayments() {
        LocalDate now = LocalDate.now();
        processExpiredPaymentsUseCase.processExpiredPayments(now);
    }
}
