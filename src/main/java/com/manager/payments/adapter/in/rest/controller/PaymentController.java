package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.PaymentDto;
import com.manager.payments.adapter.in.rest.dto.models.ReceiptDto;
import com.manager.payments.adapter.in.rest.dto.request.CreatePaymentRequestDTO;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.adapter.out.persistence.receipts.ReceiptMapper;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.application.port.out.ReceiptRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.receipts.Receipt;
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

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Payments", description = "Payments management endpoints")
@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final CreatePaymentUseCase createPaymentUseCase;
    private final PaymentMapper paymentMapper;
    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    public PaymentController(PaymentRepository paymentRepository, CreatePaymentUseCase createPaymentUseCase,
                             PaymentMapper paymentMapper, ReceiptRepository receiptRepository,
                             ReceiptMapper receiptMapper) {
        this.paymentRepository = paymentRepository;
        this.createPaymentUseCase = createPaymentUseCase;
        this.paymentMapper = paymentMapper;
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @Operation(summary = "Get all payments", description = "Support pagination via Spring Data's pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            PaymentDto.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<PaymentDto>> findAll(@ParameterObject Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return ResponseEntity.ok(PageResponse.of(payments.map(paymentMapper::toPaymentDto)));
    }

    @Operation(summary = "Get payment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            PaymentDto.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class)))
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentDto>> getPayment(@PathVariable("paymentId") UUID paymentId) {
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), paymentMapper.toPaymentDto(payment)));
    }

    @Operation(summary = "Get a payment's receipts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment's receipts",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ReceiptDto.class))),
    })
    @GetMapping("/{paymentId}/receipts")
    public ResponseEntity<ResponseDto<List<ReceiptDto>>> getPaymentReceipts(@PathVariable("paymentId") UUID playerId) {
        List<Receipt> receipts = receiptRepository.findAllByPaymentId(playerId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(),
                receipts.stream().map(receiptMapper::toReceiptDto).toList()));
    }

    @Operation(summary = "Create a new payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
            @ApiResponse(responseCode = "400", description = "Payment already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseDto<PaymentDto>> createPayment(@RequestBody CreatePaymentRequestDTO requestDTO) {
        Payment payment = createPaymentUseCase.createPayment(requestDTO);
        return ResponseEntity.created(URI.create("/v1/payments/" + payment.id())).body(new ResponseDto<>(HttpStatus.OK.value(), paymentMapper.toPaymentDto(payment)));
    }

    @Operation(summary = "Delete payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ResponseDto.class))),
    })
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<String>> deletePayment(@PathVariable UUID paymentId) {
        paymentRepository.deleteById(paymentId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "Payment with id " + paymentId + " has been" +
                " deleted."));
    }
}
