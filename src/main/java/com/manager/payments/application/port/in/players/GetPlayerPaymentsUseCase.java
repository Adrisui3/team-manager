package com.manager.payments.application.port.in.players;

import com.manager.payments.model.payments.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetPlayerPaymentsUseCase {

    Page<Payment> getPlayerPayments(UUID playerId, Pageable pageable);
}
