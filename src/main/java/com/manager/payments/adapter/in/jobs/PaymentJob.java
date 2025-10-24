package com.manager.payments.adapter.in.jobs;

import com.manager.payments.application.port.in.ProcessExpiredPaymentsUseCase;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaymentJob {

    private final ProcessExpiredPaymentsUseCase processExpiredPaymentsUseCase;

    public PaymentJob(ProcessExpiredPaymentsUseCase processExpiredPaymentsUseCase) {
        this.processExpiredPaymentsUseCase = processExpiredPaymentsUseCase;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateExpiredPayments() {
        LocalDate now = LocalDate.now();
        processExpiredPaymentsUseCase.processExpiredPayments(now);
    }
}
