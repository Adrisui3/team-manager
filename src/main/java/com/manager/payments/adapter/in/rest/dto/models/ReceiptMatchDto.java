package com.manager.payments.adapter.in.rest.dto.models;

public record ReceiptMatchDto(
        ReceiptDto receipt,
        MovementDto movement,
        boolean amountCorrect
) {
}
