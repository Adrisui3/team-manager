package com.manager.payments.application.service.players;

import com.manager.payments.application.port.in.players.GetPlayerPaymentsUseCase;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GetPlayerPaymentsService implements GetPlayerPaymentsUseCase {

    private final PlayerRepository playerRepository;
    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;

    @Override
    public Page<Payment> getPlayerPayments(UUID playerId, Pageable pageable) {
        if (!playerRepository.existsById(playerId)) {
            throw PlayerNotFoundException.byId(playerId);
        }

        return playerPaymentAssignmentRepository.findAllPaymentsByPlayerId(playerId, pageable);
    }
}
