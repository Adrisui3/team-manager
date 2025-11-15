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
import com.manager.shared.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentDto>> getPayment(@PathVariable("paymentId") UUID paymentId) {
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), paymentMapper.toPaymentDto(payment)));
    }

    @GetMapping("/{paymentId}/receipts")
    public ResponseEntity<ResponseDto<List<ReceiptDto>>> getPaymentReceipts(@PathVariable("paymentId") UUID playerId) {
        List<Receipt> receipts = receiptRepository.findAllByPaymentId(playerId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(),
                receipts.stream().map(receiptMapper::toReceiptDto).toList()));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<PaymentDto>> createPayment(@RequestBody CreatePaymentRequestDTO requestDTO) {
        Payment payment = createPaymentUseCase.createPayment(requestDTO);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), paymentMapper.toPaymentDto(payment)));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<String>> deletePayment(@PathVariable UUID paymentId) {
        paymentRepository.deleteById(paymentId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "Payment with id " + paymentId + " has been" +
                " deleted."));
    }
}
