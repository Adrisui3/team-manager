package com.manager.payments.model.movements;

import com.manager.payments.model.receipts.Receipt;
import lombok.Builder;

@Builder
public record ReceiptMatch(
        Receipt receipt,
        Movement movement,
        boolean amountCorrect
) {

    public static ReceiptMatch build(Receipt receipt, Movement movement) {
        return ReceiptMatch.builder()
                .receipt(receipt)
                .movement(movement)
                .amountCorrect(receipt.amount().compareTo(movement.amount()) == 0)
                .build();
    }
}
