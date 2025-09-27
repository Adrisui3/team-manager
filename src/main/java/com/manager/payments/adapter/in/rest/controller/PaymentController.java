package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.adapter.in.rest.dto.PaymentDto;
import com.manager.payments.adapter.out.persistence.payments.PaymentMapper;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final CreatePaymentUseCase createPaymentUseCase;
    private final PaymentMapper paymentMapper;

    public PaymentController(PaymentRepository paymentRepository, CreatePaymentUseCase createPaymentUseCase,
                             PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.createPaymentUseCase = createPaymentUseCase;
        this.paymentMapper = paymentMapper;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<PaymentDto>> getPayment(@PathVariable("paymentId") UUID paymentId) {
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(),
                paymentMapper.toPaymentDto(payment)));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<PaymentDto>> createPayment(@RequestBody CreatePaymentRequestDTO requestDTO) {
        Payment payment = createPaymentUseCase.createPayment(requestDTO);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(),
                paymentMapper.toPaymentDto(payment)));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ResponseDto<String>> deletePayment(@PathVariable UUID paymentId) {
        paymentRepository.deleteById(paymentId);
        return ResponseEntity.ok(new ResponseDto<>(LocalDateTime.now(), HttpStatus.OK.value(),
                "Payment with id " + paymentId + " has been deleted."));
    }
}
