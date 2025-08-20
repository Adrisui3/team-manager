package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.FindPaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService implements CreatePaymentUseCase, FindPaymentUseCase {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(CreatePaymentRequestDTO requestDTO) {
        Payment newPayment = PaymentFactory.build(requestDTO.amount(), requestDTO.name(), requestDTO.description(),
                requestDTO.startDate(), requestDTO.endDate(), requestDTO.periodDays());
        return paymentRepository.save(newPayment);
    }

    @Override
    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }
}
