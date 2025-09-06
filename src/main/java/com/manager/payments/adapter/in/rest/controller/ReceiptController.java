package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.receipts.ReceiptStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;

    public ReceiptController(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @PutMapping("/{receiptId}/update-status/{newStatus}")
    public void updatePaymentStatus(@PathVariable UUID receiptId, @PathVariable ReceiptStatus newStatus) {
        receiptRepository.updateStatus(receiptId, newStatus);
    }
}
