package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.application.exception.PaymentNotFoundException;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.FindPaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class PaymentService implements CreatePaymentUseCase, FindPaymentUseCase {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(CreatePaymentRequestDTO requestDTO) {
        Payment newPayment = new Payment(null, requestDTO.amount(), requestDTO.name(), requestDTO.description(),
                requestDTO.startDate(), requestDTO.startDate(), requestDTO.endDate(), requestDTO.periodDays(),
                PaymentStatus.ACTIVE, Collections.emptyList());
        return paymentRepository.save(newPayment);
    }

    @Override
    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }
}
