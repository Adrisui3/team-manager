package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.FindPaymentUseCase;
import com.manager.payments.model.payments.Payment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PaymentController {

    private final FindPaymentUseCase findPaymentUseCase;
    private final CreatePaymentUseCase createPaymentUseCase;

    public PaymentController(FindPaymentUseCase findPaymentUseCase, CreatePaymentUseCase createPaymentUseCase) {
        this.findPaymentUseCase = findPaymentUseCase;
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @GetMapping("/payment/{paymentId}")
    public Payment getPayment(@PathVariable("paymentId") UUID paymentId) {
        return findPaymentUseCase.findById(paymentId);
    }

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody CreatePaymentRequestDTO requestDTO) {
        return createPaymentUseCase.createPayment(requestDTO);
    }
}
