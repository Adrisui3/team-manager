package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.ReceiptNotFoundException;
import com.manager.payments.model.receipts.Receipt;
import com.manager.payments.model.receipts.ReceiptStatus;
import com.manager.shared.response.PageResponse;
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Receipts", description = "Receipts management endpoints")
@RestController
@RequestMapping("/v1/receipts")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptController(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @Operation(summary = "Get all receipts", description = "Supports pagination via Spring Data's pageable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of receipts",
                    useReturnTypeSchema = true)
    })
    @GetMapping
    public ResponseEntity<PageResponse<ReceiptDto>> getAllReceipts(@ParameterObject Pageable pageable) {
        Page<Receipt> receipts = receiptRepository.findAll(pageable);
        return ResponseEntity.ok(PageResponse.of(receipts.map(receiptMapper::toReceiptDto)));
    }

    @Operation(summary = "Get a receipt by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt found", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Receipt not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{receiptId}")
    public ResponseEntity<ResponseDto<ReceiptDto>> getReceipt(@PathVariable("receiptId") UUID receiptId) {
        Receipt receipt =
                receiptRepository.findById(receiptId).orElseThrow(() -> new ReceiptNotFoundException(receiptId));
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), receiptMapper.toReceiptDto(receipt)));
    }

    @Operation(summary = "Update the status of a receipt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receipt' status updated", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Receipt not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/{receiptId}/update-status/{newStatus}")
    public ResponseEntity<ResponseDto<ReceiptDto>> updateReceiptStatus(@PathVariable UUID receiptId,
                                                                       @PathVariable ReceiptStatus newStatus) {
        Receipt updatedReceipt = receiptRepository.updateStatus(receiptId, newStatus);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), receiptMapper.toReceiptDto(updatedReceipt)));
    }
}
