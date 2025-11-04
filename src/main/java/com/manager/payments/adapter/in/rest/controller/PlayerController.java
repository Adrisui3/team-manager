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
import com.manager.payments.application.port.out.PlayerRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.exceptions.PlayerNotFoundException;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.receipts.Receipt;
import com.manager.shared.response.PageResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Players", description = "Players management endpoints")
@RestController
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final PlayerRepository playerRepository;
    private final AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;
    private final PlayerMapper playerMapper;
    private final ReceiptMapper receiptMapper;
    private final ReceiptRepository receiptRepository;

    public PlayerController(PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper,
                            CreatePlayerUseCase createPlayerUseCase, PlayerRepository playerRepository,
                            AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase, PlayerMapper playerMapper,
                            ReceiptMapper receiptMapper, ReceiptRepository receiptRepository) {
        this.playerPaymentAssignmentMapper = playerPaymentAssignmentMapper;
        this.createPlayerUseCase = createPlayerUseCase;
        this.playerRepository = playerRepository;
        this.assignPaymentToPlayerUseCase = assignPaymentToPlayerUseCase;
        this.playerMapper = playerMapper;
        this.receiptMapper = receiptMapper;
        this.receiptRepository = receiptRepository;
    }

    @Operation(summary = "Get all players", description = "Supports pagination via Spring Data's pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of players",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            PlayerDto.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<PlayerDto>> findAll(@ParameterObject Pageable pageable) {
        Page<Player> players = playerRepository.findAllPlayers(pageable);
        return ResponseEntity.ok(PageResponse.of(players.map(playerMapper::toPlayerDto)));
    }

    @Operation(summary = "Get player by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            PlayerDto.class))),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @GetMapping("/{playerId}")
    public ResponseEntity<ResponseDto<PlayerDto>> getPlayer(@PathVariable UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), playerMapper.toPlayerDto(player)));
    }

    @Operation(summary = "Get a player's receipts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player's receipts",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ReceiptDto.class))),
    })
    @GetMapping("/{playerId}/receipts")
    public ResponseEntity<ResponseDto<List<ReceiptDto>>> getPlayerReceipts(@PathVariable UUID playerId) {
        List<Receipt> receipts = receiptRepository.findAllByPlayerId(playerId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(),
                receipts.stream().map(receiptMapper::toReceiptDto).toList()));
    }

    @Operation(summary = "Create a new player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerDto.class))),
            @ApiResponse(responseCode = "400", description = "Player already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseDto<PlayerDto>> createUser(@Valid @RequestBody CreatePlayerRequestDTO requestDTO) {
        Player newPlayer = createPlayerUseCase.createPlayer(requestDTO);
        return ResponseEntity.created(URI.create("/players/" + newPlayer.id())).body(new ResponseDto<>(HttpStatus.CREATED.value(), playerMapper.toPlayerDto(newPlayer)));
    }

    @Operation(summary = "Assigns payment to player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assignment created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlayerPaymentAssignmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Assignment already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @PostMapping("/{playerId}/assign/{paymentId}")
    public ResponseEntity<ResponseDto<PlayerPaymentAssignmentDto>> assignPaymentToPlayer(@PathVariable UUID playerId,
                                                                                         @PathVariable UUID paymentId) {
        PlayerPaymentAssignment assignment = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED.value(),
                playerPaymentAssignmentMapper.toPlayerPaymentAssignmentDto(assignment)));
    }

    @Operation(summary = "Delete player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
    })
    @DeleteMapping("/{playerId}")
    public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable UUID playerId) {
        playerRepository.deleteById(playerId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "Player with id " + playerId + " was " +
                "deleted"));
    }
}
