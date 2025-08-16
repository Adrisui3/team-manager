package com.manager.payments.adapter.in.rest.jobs;

import com.manager.payments.application.port.in.CreateReceiptUseCase;
import com.manager.payments.model.receipts.ReceiptMinInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BillingJob {

    private final CreateReceiptUseCase createReceiptUseCase;

    public BillingJob(CreateReceiptUseCase createReceiptUseCase) {
        this.createReceiptUseCase = createReceiptUseCase;
    }

    @PostMapping("/receipt/{userId}/{paymentId}")
    public ReceiptMinInfo createReceipt(@PathVariable UUID userId, @PathVariable UUID paymentId) {
        return createReceiptUseCase.createReceipt(userId, paymentId);
    }
}
