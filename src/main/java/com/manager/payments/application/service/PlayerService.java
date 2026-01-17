package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.adapter.in.rest.dto.request.UpdatePlayerRequestDTO;
import com.manager.payments.application.port.in.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.in.CreatePlayerUseCase;
import com.manager.payments.application.port.in.FindPlayerUseCase;
import com.manager.payments.application.port.in.UpdatePlayerUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.assignments.PlayerPaymentAssignmentFactory;
import com.manager.payments.model.exceptions.*;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlayerService implements CreatePlayerUseCase, AssignPaymentToPlayerUseCase, UpdatePlayerUseCase,
        FindPlayerUseCase {

    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    @Override
    public Player createPlayer(CreatePlayerRequestDTO requestDTO) {
        if (playerRepository.existsByPersonalId(requestDTO.personalId()))
            throw PlayerAlreadyExistsException.byPersonalId(requestDTO.personalId());

        if (playerRepository.existsByEmail(requestDTO.email())) {
            throw PlayerAlreadyExistsException.byEmail(requestDTO.email());
        }

        Player newPlayer = Player.builder()
                .personalId(requestDTO.personalId())
                .name(requestDTO.name())
                .surname(requestDTO.surname())
                .email(requestDTO.email())
                .birthDate(requestDTO.birthDate())
                .category(requestDTO.category())
                .status(PlayerStatus.ENABLED)
                .build();

        return playerRepository.save(newPlayer);
    }

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

    @Override
    public Player updatePlayer(UUID playerId, UpdatePlayerRequestDTO request) {
        Player player =
                playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));

        if (!request.email().equals(player.email()) && playerRepository.existsByEmail(request.email())) {
            throw PlayerAlreadyExistsException.byEmail(request.email());
        }

        Player updatedPlayer = player.toBuilder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .birthDate(request.birthDate())
                .category(request.category())
                .status(request.status())
                .build();

        return playerRepository.save(updatedPlayer);
    }

    @Override
    public Player findById(UUID playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));
    }

    @Override
    public Page<Player> findAll(String query, Pageable pageable) {
        return playerRepository.findAllByQuery(query.trim().toLowerCase(Locale.ROOT), pageable);
    }
}
