package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.in.receipts.DeleteReceiptUseCase;
import com.manager.payments.application.port.in.receipts.FindReceiptUseCase;
import com.manager.payments.application.port.in.receipts.UpdateReceiptStatusUseCase;
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
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Receipts", description = "Receipts management endpoints")
@RestController
@RequestMapping("/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptMapper mapper;
    private final UpdateReceiptStatusUseCase updateReceiptStatusUseCase;
    private final DeleteReceiptUseCase deleteReceiptUseCase;
    private final FindReceiptUseCase findReceiptUseCase;

    @Operation(summary = "Get all receipts", description = "Supports pagination via Spring Data's pageable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of receipts",
                    useReturnTypeSchema = true)
    })
    @GetMapping
    public ResponseEntity<PageResponse<ReceiptDto>> getAllReceipts(@RequestParam(name = "query", required = false,
                                                                               defaultValue = "") String query,
                                                                   @RequestParam(name = "status", required = false) ReceiptStatus status,
                                                                   @ParameterObject Pageable pageable) {
        Page<Receipt> receipts = findReceiptUseCase.findAllByQuery(query, status, pageable);
        return ResponseEntity.ok(PageResponse.of(receipts.map(mapper::toReceiptDto)));
    }

    @Operation(summary = "Get a receipt by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt found", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Receipt not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{receiptId}")
    public ResponseEntity<ResponseDto<ReceiptDto>> getReceipt(@PathVariable("receiptId") UUID receiptId) {
        Receipt receipt = findReceiptUseCase.findById(receiptId);
        return ResponseEntity.ok(new ResponseDto<>(mapper.toReceiptDto(receipt)));
    }

    @Operation(summary = "Update the status of a receipt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt' status updated", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Receipt not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{receiptId}/update-status")
    public ResponseEntity<ResponseDto<ReceiptDto>> updateReceiptStatus(@PathVariable UUID receiptId,
                                                                       @RequestParam(name = "newStatus") ReceiptStatus newStatus) {
        Receipt updatedReceipt = updateReceiptStatusUseCase.updateStatus(receiptId, newStatus);
        return ResponseEntity.ok(new ResponseDto<>(mapper.toReceiptDto(updatedReceipt)));
    }

    @Operation(summary = "Delete a receipt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt deleted", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Receipt not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{receiptId}")
    public ResponseEntity<ResponseDto<String>> deleteReceipt(@PathVariable UUID receiptId) {
        deleteReceiptUseCase.deleteReceipt(receiptId);
        return ResponseEntity.ok(new ResponseDto<>("Receipt with id " + receiptId + " has been deleted"));
    }
}
