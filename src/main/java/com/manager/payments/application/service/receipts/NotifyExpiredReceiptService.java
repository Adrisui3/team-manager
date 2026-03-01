package com.manager.payments.application.service.receipts;

import com.manager.email.application.port.in.SendExpiredReceiptEmailUseCase;
import com.manager.email.model.ExpiredReceiptEmailRequest;
import com.manager.payments.application.port.in.receipts.NotifyExpiredReceiptUseCase;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyExpiredReceiptService implements NotifyExpiredReceiptUseCase {

    private final ReceiptRepository repository;
    private final SendExpiredReceiptEmailUseCase sendExpiredReceiptEmailUseCase;

    @Override
    public void notifyExpiredReceipts(List<UUID> expiredReceiptsIds) {
        expiredReceiptsIds.forEach(receiptId -> {
            try {
                notifyExpiredReceipt(receiptId);
            } catch (ReceiptNotFoundException e) {
                log.error("Receipt with id {} was not found", receiptId);
            }
        });
    }

    private void notifyExpiredReceipt(UUID receiptId) {
        Receipt receipt = repository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        if (receipt.status() != ReceiptStatus.OVERDUE) {
            log.warn("Skipping notification for receipt {}, status: {}", receipt.id(), receipt.status());
            return;
        }

        ExpiredReceiptEmailRequest request = ExpiredReceiptEmailRequest.buildFromReceipt(receipt);
        sendExpiredReceiptEmailUseCase.sendExpiredReceiptEmail(request);
    }
}
