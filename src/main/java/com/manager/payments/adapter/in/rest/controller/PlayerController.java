package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.PaymentDto;
import com.manager.payments.adapter.in.rest.dto.models.PlayerDto;
import com.manager.payments.adapter.in.rest.dto.models.PlayerPaymentAssignmentDto;
import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.in.rest.dto.request.CreatePlayerRequestDTO;
import com.manager.payments.adapter.in.rest.dto.request.UpdatePlayerRequestDTO;
import com.manager.payments.adapter.out.persistence.assignments.PlayerPaymentAssignmentMapper;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.players.PlayerMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.in.players.*;
import com.manager.payments.model.assignments.PlayerPaymentAssignment;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.Player;
import com.manager.payments.model.players.PlayerGender;
import com.manager.payments.model.players.PlayerStatus;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import com.manager.shared.response.ErrorResponse;
import com.manager.shared.response.PageResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Players", description = "Players management endpoints")
@RestController
@RequestMapping("/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerPaymentAssignmentMapper playerPaymentAssignmentMapper;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final AssignPaymentToPlayerUseCase assignPaymentToPlayerUseCase;
    private final PlayerMapper playerMapper;
    private final ReceiptMapper receiptMapper;
    private final PaymentMapper paymentMapper;
    private final UpdatePlayerUseCase updatePlayerUseCase;
    private final FindPlayerUseCase findPlayerUseCase;
    private final GetPlayerReceiptsUseCase getPlayerReceiptsUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;
    private final GetPlayerPaymentsUseCase getPlayerPaymentsUseCase;

    @Operation(summary = "Get all players", description = "Supports pagination and query searching for personal ID, " +
            "name and surname")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of players", useReturnTypeSchema = true)
    })
    @GetMapping
    public ResponseEntity<PageResponse<PlayerDto>> findAll(@RequestParam(name = "query", required = false,
                                                                       defaultValue = "") String query,
                                                           @RequestParam(name = "category", required = false) Category category,
                                                           @RequestParam(name = "gender", required = false) PlayerGender gender,
                                                           @RequestParam(name = "status", required = false) PlayerStatus status,
                                                           @ParameterObject Pageable pageable) {
        Page<Player> players = findPlayerUseCase.findAll(query, category, gender, status, pageable);
        return ResponseEntity.ok(PageResponse.of(players.map(playerMapper::toPlayerDto)));
    }

    @Operation(summary = "Get player by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @GetMapping("/{playerId}")
    public ResponseEntity<ResponseDto<PlayerDto>> getPlayer(@PathVariable("playerId") UUID playerId) {
        Player player = findPlayerUseCase.findById(playerId);
        return ResponseEntity.ok(new ResponseDto<>(playerMapper.toPlayerDto(player)));
    }

    @Operation(summary = "Get a player's receipts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player's receipts", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @GetMapping("/{playerId}/receipts")
    public ResponseEntity<PageResponse<ReceiptDto>> getPlayerReceipts(@PathVariable("playerId") UUID playerId,
                                                                      @RequestParam(name = "status",
                                                                              required = false) ReceiptStatus status,
                                                                      @ParameterObject Pageable pageable) {
        Page<Receipt> receipts = getPlayerReceiptsUseCase.getPlayerReceipts(playerId, status, pageable);
        return ResponseEntity.ok(PageResponse.of(receipts.map(receiptMapper::toReceiptDto)));
    }

    @Operation(summary = "Create a new player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "Player already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseDto<PlayerDto>> createPlayer(@Valid @RequestBody CreatePlayerRequestDTO requestDTO) {
        Player newPlayer = createPlayerUseCase.createPlayer(requestDTO);
        return ResponseEntity.created(URI.create("/v1/players/" + newPlayer.id())).body(new ResponseDto<>(playerMapper.toPlayerDto(newPlayer)));
    }

    @Operation(summary = "Assigns payment to player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assignment created",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player or payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Assignment already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @PostMapping("/{playerId}/assign/{paymentId}")
    public ResponseEntity<ResponseDto<PlayerPaymentAssignmentDto>> assignPaymentToPlayer(@PathVariable("playerId") UUID playerId,
                                                                                         @PathVariable("paymentId") UUID paymentId) {
        PlayerPaymentAssignment assignment = assignPaymentToPlayerUseCase.assignPaymentToPlayer(playerId, paymentId);
        return ResponseEntity.created(URI.create("/v1/players/" + playerId + "/payments")).body(new ResponseDto<>(
                playerPaymentAssignmentMapper.toPlayerPaymentAssignmentDto(assignment)));
    }

    @Operation(summary = "Get payments assigned to a player", description = "Supports pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments assigned to the player",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
    })
    @GetMapping("/{playerId}/payments")
    public ResponseEntity<PageResponse<PaymentDto>> getPlayerPayments(@PathVariable("playerId") UUID playerId,
                                                                      @ParameterObject Pageable pageable) {
        Page<Payment> payments = getPlayerPaymentsUseCase.getPlayerPayments(playerId, pageable);
        return ResponseEntity.ok(PageResponse.of(payments.map(paymentMapper::toPaymentDto)));
    }

    @Operation(summary = "Unassigns a payment from a given player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment deleted",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player, payment or assignment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
    })
    @DeleteMapping("/{playerId}/unassign/{paymentId}")
    public ResponseEntity<ResponseDto<String>> unassignPaymentFromPlayer(@PathVariable("playerId") UUID playerId,
                                                                         @PathVariable("paymentId") UUID paymentId) {
        assignPaymentToPlayerUseCase.unassignPaymentToPlayer(playerId, paymentId);
        return ResponseEntity.ok(new ResponseDto<>("Payment with id " + playerId + " was " +
                "unassigned from player with id " + playerId));
    }

    @Operation(summary = "Delete player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player deleted",
                    useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
    })
    @DeleteMapping("/{playerId}")
    public ResponseEntity<ResponseDto<String>> deletePlayer(@PathVariable("playerId") UUID playerId) {
        deletePlayerUseCase.deleteById(playerId);
        return ResponseEntity.ok(new ResponseDto<>("Player with id " + playerId + " was " +
                "deleted"));
    }

    @Operation(summary = "Update player data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Player not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation =
                    ErrorResponse.class)))
    })
    @PutMapping("/{playerId}")
    public ResponseEntity<ResponseDto<PlayerDto>> updatePlayer(@PathVariable("playerId") UUID playerId,
                                                               @Valid @RequestBody UpdatePlayerRequestDTO requestDTO) {
        Player updatedPlayer = updatePlayerUseCase.updatePlayer(playerId, requestDTO);
        return ResponseEntity.ok(new ResponseDto<>(playerMapper.toPlayerDto(updatedPlayer)));
    }
}
