package com.manager.payments.model.users;

import com.manager.payments.model.exceptions.PaymentNotAssignedException;
import com.manager.payments.model.exceptions.PlayerNotAssignedException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentMinInfo;
import com.manager.payments.model.receipts.ReceiptFactory;
import com.manager.payments.model.receipts.ReceiptMinInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Player(UUID id, String personalId, String name, String surname, String email, LocalDate birthDate,
                     Category category, PlayerStatus status, List<PaymentMinInfo> payments,
                     List<ReceiptMinInfo> receipts) {

    public boolean hasPayment(UUID paymentId) {
        return payments().stream().anyMatch(p -> p.id().equals(paymentId));
    }

    public void createReceiptFor(Payment payment) {
        if (!hasPayment(payment.id())) {
            throw new PaymentNotAssignedException(id(), payment.id());
        }

        if (!payment.hasPlayer(id())) {
            throw new PlayerNotAssignedException(id(), payment.id());
        }

        ReceiptMinInfo receipt = ReceiptFactory.buildMinInfo(payment);
        receipts().add(receipt);
    }
}
