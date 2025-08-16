package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.Payment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final CreatePaymentUseCase createPaymentUseCase;

    public PaymentController(PaymentRepository paymentRepository, CreatePaymentUseCase createPaymentUseCase) {
        this.paymentRepository = paymentRepository;
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @GetMapping("/payment/{paymentId}")
    public Payment getPayment(@PathVariable("paymentId") UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody CreatePaymentRequestDTO requestDTO) {
        return createPaymentUseCase.createPayment(requestDTO);
    }
}
