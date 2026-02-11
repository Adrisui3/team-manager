package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.adapter.in.rest.dto.request.UpdatePlayerRequestDTO;
import com.manager.payments.application.port.in.players.*;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.assignments.PlayerPaymentAssignmentFactory;
import com.manager.payments.model.exceptions.*;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
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
        FindPlayerUseCase, GetPlayerReceiptsUseCase, DeletePlayerUseCase, GetPlayerPaymentsUseCase {

    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;
    private final ReceiptRepository receiptRepository;

    @Override
    public Player createPlayer(CreatePlayerRequestDTO request) {
        if (playerRepository.existsByPersonalId(request.personalId()))
            throw PlayerAlreadyExistsException.byPersonalId(request.personalId());

        Player newPlayer = Player.builder()
                .personalId(request.personalId())
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .secondaryEmail(request.secondaryEmail())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .secondaryPhoneNumber(request.secondaryPhoneNumber())
                .category(request.category())
                .gender(request.gender())
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

        Player updatedPlayer = player.toBuilder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .secondaryEmail(request.secondaryEmail())
                .birthDate(request.birthDate())
                .phoneNumber(request.phoneNumber())
                .secondaryPhoneNumber(request.secondaryPhoneNumber())
                .category(request.category())
                .status(request.status())
                .gender(request.gender())
                .build();

        return playerRepository.save(updatedPlayer);
    }

    @Override
    public Player findById(UUID playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> PlayerNotFoundException.byId(playerId));
    }

    @Override
    public Page<Player> findAll(String query, Category category, PlayerGender gender, PlayerStatus status,
                                Boolean hasPendingReceipt, Boolean withoutPaymentAssigned, Boolean hasOverdueReceipt,
                                Pageable pageable) {
        return playerRepository.findAll(query.trim().toLowerCase(Locale.ROOT), category, gender, status,
                hasPendingReceipt, withoutPaymentAssigned, hasOverdueReceipt, pageable);
    }

    @Override
    public Page<Receipt> getPlayerReceipts(UUID playerId, ReceiptStatus status, Pageable pageable) {
        if (!playerRepository.existsById(playerId)) {
            throw PlayerNotFoundException.byId(playerId);
        }

        return status == null ? receiptRepository.findAllByPlayerId(playerId, pageable) :
                receiptRepository.findAllByPlayerIdAndStatus(playerId, pageable, status);
    }

    @Override
    public void deleteById(UUID id) {
        if (!playerRepository.existsById(id)) {
            throw PlayerNotFoundException.byId(id);
        }

        playerRepository.deleteById(id);
    }

    @Override
    public Page<Payment> getPlayerPayments(UUID playerId, Pageable pageable) {
        if (!playerRepository.existsById(playerId)) {
            throw PlayerNotFoundException.byId(playerId);
        }

        return playerPaymentAssignmentRepository.findAllPaymentsByPlayerId(playerId, pageable);
    }
}
