package com.manager.payments.application.port.out;

import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerPaymentAssignmentRepository {

    PlayerPaymentAssignment save(PlayerPaymentAssignment playerPaymentAssignment);

    Optional<PlayerPaymentAssignment> findById(UUID id);

    void addReceipt(UUID playerPaymentAssignmentId, Receipt receipt);

    boolean existsByPlayerAndPayment(Player player, Payment payment);

    List<PlayerPaymentAssignment> findAllActiveAndStartDateBeforeOrEqual(LocalDate date);
}
