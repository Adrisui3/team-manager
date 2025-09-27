package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePlayerRequestDTO;
import com.manager.payments.adapter.in.rest.dto.PlayerDto;
import com.manager.payments.adapter.in.rest.dto.ReceiptMinInfoDto;
import com.manager.payments.adapter.out.persistence.players.PlayerMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.in.AssignPaymentToPlayerUseCase;
import com.manager.payments.application.port.in.CreatePlayerUseCase;
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final PlayerRepository playerRepository;
    private final AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;
    private final PlayerMapper playerMapper;
    private final ReceiptMapper receiptMapper;

    public PlayerController(CreatePlayerUseCase createPlayerUseCase, PlayerRepository playerRepository,
                            AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase, PlayerMapper playerMapper,
                            ReceiptMapper receiptMapper) {
        this.createPlayerUseCase = createPlayerUseCase;
        this.playerRepository = playerRepository;
        this.assignPaymentToPlayerUseCase = assignPaymentToPlayerUseCase;
        this.playerMapper = playerMapper;
        this.receiptMapper = receiptMapper;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<ResponseDto<PlayerDto>> getUser(@PathVariable("playerId") UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(),
                playerMapper.toPlayerDto(player)));
    }

    @GetMapping("/{playerId}/receipts")
    public ResponseEntity<ResponseDto<List<ReceiptMinInfoDto>>> getUserReceipts(@PathVariable("playerId") UUID playerId) {
        List<ReceiptMinInfoDto> receiptMinInfoDtos =
                receiptMapper.toReceiptMinInfoDto(playerRepository.findAllReceipts(playerId));
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(), receiptMinInfoDtos));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<PlayerDto>> createUser(@RequestBody CreatePlayerRequestDTO requestDTO) {
        Player newPlayer = createPlayerUseCase.createPlayer(requestDTO);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(),
                playerMapper.toPlayerDto(newPlayer)));
    }

    @PutMapping("/{playerId}/assign/{paymentId}")
    public ResponseEntity<ResponseDto<Player>> assignPaymentToPlayer(@PathVariable UUID playerId,
                                                                     @PathVariable UUID paymentId) {
        Player updatedPlayer = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(), updatedPlayer));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable UUID playerId) {
        playerRepository.deleteById(playerId);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(),
                HttpStatus.OK.value(), "Player with id " + playerId + " was deleted"));
    }
}
