package com.manager.payments.generator;

import com.manager.payments.model.movements.Movement;
import com.manager.payments.model.movements.ReceiptMatch;
import com.manager.payments.model.receipts.Receipt;

public final class ReceiptMatchGenerator {

    private final ReceiptMatch.ReceiptMatchBuilder builder;

    private ReceiptMatchGenerator() {
        this.builder = ReceiptMatch.builder();
    }

    public static ReceiptMatchGenerator receiptMatch() {
        ReceiptMatchGenerator generator = new ReceiptMatchGenerator();
        generator.receipt(ReceiptGenerator.receipt().build());
        generator.movement(MovementGenerator.movement().build());
        generator.amountCorrect(true);
        return generator;
    }

    public ReceiptMatchGenerator receipt(Receipt receipt) {
        builder.receipt(receipt);
        return this;
    }

    public ReceiptMatchGenerator movement(Movement movement) {
        builder.movement(movement);
        return this;
    }

    public ReceiptMatchGenerator amountCorrect(boolean amountCorrect) {
        builder.amountCorrect(amountCorrect);
        return this;
    }

    public ReceiptMatch build() {
        return builder.build();
    }
}
