package com.manager.payments.application.service.players;

import com.manager.payments.application.port.in.players.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.assignments.PlayerPaymentAssignmentFactory;
import com.manager.payments.model.exceptions.AssignmentAlreadyExistsException;
import com.manager.payments.model.exceptions.AssignmentNotFoundException;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AssignPaymentToPlayerService implements AssignPaymentToPlayerUseCase {

    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    @Override
    public PlayerPaymentAssignment assignPaymentToPlayer(UUID playerId, UUID paymentId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (playerPaymentAssignmentRepository.existsByPlayerIdAndPaymentId(playerId, paymentId)) {
            throw new AssignmentAlreadyExistsException(playerId, paymentId);
        }

        PlayerPaymentAssignment playerPaymentAssignment = PlayerPaymentAssignmentFactory.build(player, payment);
        return playerPaymentAssignmentRepository.save(playerPaymentAssignment);
    }

    @Override
    public void unassignPaymentToPlayer(UUID playerId, UUID paymentId) {
        if (!playerRepository.existsById(playerId)) {
            throw PlayerNotFoundException.byId(playerId);
        }

        if (!paymentRepository.existsById(paymentId)) {
            throw new PaymentNotFoundException(paymentId);
        }

        if (!playerPaymentAssignmentRepository.existsByPlayerIdAndPaymentId(playerId, paymentId)) {
            throw new AssignmentNotFoundException(playerId, paymentId);
        }

        playerPaymentAssignmentRepository.deleteByPlayerIdAndPaymentId(playerId, paymentId);
    }
}
