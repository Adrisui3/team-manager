package com.manager.payments.model.users;

import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.receipts.ReceiptMinInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record User(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                   Category category, UserStatus status, List<PaymentMinInfo> payments, List<ReceiptMinInfo> receipts) {

    public boolean hasPayment(UUID paymentId) {
        return payments().stream().anyMatch(p -> p.id().equals(paymentId));
    }
}
