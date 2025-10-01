package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.PlayerDto;
import com.manager.payments.adapter.in.rest.dto.models.PlayerPaymentAssignmentDto;
import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentMapper;
import com.manager.payments.adapter.out.persistence.players.PlayerMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.in.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.in.CreatePlayerUseCase;
import com.manager.payments.application.port.out.PlayerPaymentAssignmentRepository;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository;
    private final PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final PlayerRepository playerRepository;
    private final AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;
    private final PlayerMapper playerMapper;
    private final ReceiptMapper receiptMapper;

    public PlayerController(PlayerPaymentAssignmentRepository playerPaymentAssignmentRepository,
                            PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper,
                            CreatePlayerUseCase createPlayerUseCase, PlayerRepository playerRepository,
                            AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase, PlayerMapper playerMapper,
                            ReceiptMapper receiptMapper) {
        this.playerPaymentAssignmentRepository = playerPaymentAssignmentRepository;
        this.playerPaymentAssignmentMapper = playerPaymentAssignmentMapper;
        this.createPlayerUseCase = createPlayerUseCase;
        this.playerRepository = playerRepository;
        this.assignPaymentToPlayerUseCase = assignPaymentToPlayerUseCase;
        this.playerMapper = playerMapper;
        this.receiptMapper = receiptMapper;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<ResponseDto<PlayerDto>> getUser(@PathVariable("playerId") UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), playerMapper.toPlayerDto(player)));
    }

    @GetMapping("/{playerId}/receipts")
    public ResponseEntity<ResponseDto<List<ReceiptDto>>> getUserReceipts(@PathVariable("playerId") UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        List<Receipt> receipts = playerPaymentAssignmentRepository.findAllReceiptsByPlayer(player);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(),
                receipts.stream().map(receiptMapper::toReceiptDto).toList()));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<PlayerDto>> createUser(@RequestBody CreatePlayerRequestDTO requestDTO) {
        Player newPlayer = createPlayerUseCase.createPlayer(requestDTO);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), playerMapper.toPlayerDto(newPlayer)));
    }

    @PostMapping("/{playerId}/assign/{paymentId}")
    public ResponseEntity<ResponseDto<PlayerPaymentAssignmentDto>> assignPaymentToPlayer(@PathVariable UUID playerId,
                                                                                         @PathVariable UUID paymentId) {
        PlayerPaymentAssignment assignment = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(),
                playerPaymentAssignmentMapper.toPlayerPaymentAssignmentDto(assignment)));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable UUID playerId) {
        playerRepository.deleteById(playerId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "Player with id " + playerId + " was " +
                "deleted"));
    }
}
