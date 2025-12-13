package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.application.port.in.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.in.CreatePlayerUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.assignments.PlayerPaymentAssignmentFactory;
import com.manager.payments.model.exceptions.*;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PlayerService implements CreatePlayerUseCase, AssignPaymentToPlayerUseCase {

    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository,
                         PaymentRepository paymentRepository,
                         PlayerRepository playerRepository) {
        this.playerPaymentAssignmentRepository = playerPaymentAssignmentRepository;
        this.paymentRepository = paymentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(CreatePlayerRequestDTO requestDTO) {
        Optional<Player> existingPlayer =
                playerRepository.findByPersonalId(requestDTO.personalId());
        if (existingPlayer.isPresent()) {
            throw new PlayerAlreadyExistsException(requestDTO.personalId());
        }

        Player newPlayer = new Player(requestDTO.personalId(), requestDTO.name(), requestDTO.surname(),
                requestDTO.email(), requestDTO.birthDate(), requestDTO.category(), PlayerStatus.ENABLED);
        return playerRepository.save(newPlayer);
    }

    @Override
    public PlayerPaymentAssignment assignPaymentToPlayer(UUID playerId, UUID paymentId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
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
            throw new PlayerNotFoundException(playerId);
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
