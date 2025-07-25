package com.manager.payments.adapter.in.rest.controller;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.model.payments.Payment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;

    public PaymentController(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody CreatePaymentRequestDTO requestDTO) {
        return createPaymentUseCase.createPayment(requestDTO);
    }
}
