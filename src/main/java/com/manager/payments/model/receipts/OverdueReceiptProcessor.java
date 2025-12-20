package com.manager.payments.model.receipts;

import java.util.List;

public class OverdueReceiptProcessor {

    private OverdueReceiptProcessor() {
    }

    public static List<Receipt> process(List<Receipt> receipts) {
        return receipts.stream().map(receipt -> receipt.toBuilder().status(ReceiptStatus.OVERDUE).build()).toList();
    }
}
