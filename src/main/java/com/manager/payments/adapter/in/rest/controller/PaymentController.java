package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.models.PaymentDto;
import com.manager.payments.adapter.in.rest.dto.request.CreatePaymentRequestDTO;
import com.manager.payments.adapter.in.rest.dto.request.UpdatePaymentRequestDTO;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.DeletePaymentUseCase;
import com.manager.payments.application.port.in.FindPaymentUseCase;
import com.manager.payments.application.port.in.UpdatePaymentUseCase;
import com.manager.payments.model.payments.Payment;
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
import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Payments", description = "Payments management endpoints")
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final UpdatePaymentUseCase updatePaymentUseCase;
    private final PaymentMapper paymentMapper;
    private final FindPaymentUseCase findPaymentUseCase;
    private final DeletePaymentUseCase deletePaymentUseCase;

    @Operation(summary = "Get all payments", description = "Support pagination via Spring Data's pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments", useReturnTypeSchema = true)
    })
    @GetMapping
    public ResponseEntity<PageResponse<PaymentDto>> findAll(@RequestParam(name = "query", required = false,
                                                                        defaultValue = "") String query,
                                                            @ParameterObject Pageable pageable) {
        Page<Payment> payments = findPaymentUseCase.findAll(query, pageable);
        return ResponseEntity.ok(PageResponse.of(payments.map(paymentMapper::toPaymentDto)));
    }

    @Operation(summary = "Get payment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class)))
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentDto>> getPayment(@PathVariable UUID paymentId) {
        Payment payment = findPaymentUseCase.findById(paymentId);
        return ResponseEntity.ok(new ResponseDto<>(paymentMapper.toPaymentDto(payment)));
    }

    @Operation(summary = "Create a new payment", description = "Unique payments must not have a billing interval, " +
            "whereas periodic ones must have it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "Payment already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Either unique payment has a billing interval defined or" +
                    " a periodic payment doesn't have it",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseDto<PaymentDto>> createPayment(@Valid @RequestBody CreatePaymentRequestDTO requestDTO) {
        Payment payment = createPaymentUseCase.createPayment(requestDTO);
        return ResponseEntity.created(URI.create("/v1/payments/" + payment.id())).body(new ResponseDto<>(paymentMapper.toPaymentDto(payment)));
    }

    @Operation(summary = "Delete payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment deleted", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            ErrorResponse.class))),
    })
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<String>> deletePayment(@PathVariable UUID paymentId) {
        deletePaymentUseCase.deleteById(paymentId);
        return ResponseEntity.ok(new ResponseDto<>("Payment with id " + paymentId + " has been" +
                " deleted."));
    }

    @Operation(summary = "Update payment data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment updated", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "New payment status cannot be set to EXPIRED or a " +
                    "payment cannot be activated outside its billing period", content = @Content(mediaType =
                    "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentDto>> updatePayment(@PathVariable UUID paymentId,
                                                                 @Valid @RequestBody UpdatePaymentRequestDTO requestDTO) {
        LocalDate currentDate = LocalDate.now();
        Payment updatedPayment = updatePaymentUseCase.updatePayment(paymentId, requestDTO, currentDate);
        return ResponseEntity.ok(new ResponseDto<>(paymentMapper.toPaymentDto(updatedPayment)));
    }
}
