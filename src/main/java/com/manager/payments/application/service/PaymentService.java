package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.CreatePaymentRequestDTO;
import com.manager.payments.adapter.in.rest.dto.request.UpdatePaymentRequestDTO;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.ProcessExpiredPaymentsUseCase;
import com.manager.payments.application.port.in.UpdatePaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentAlreadyExistsException;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.ExpiredPaymentProcessor;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements CreatePaymentUseCase, ProcessExpiredPaymentsUseCase, UpdatePaymentUseCase {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(CreatePaymentRequestDTO requestDTO) {
        if (paymentRepository.existsByCode(requestDTO.code()))
            throw new PaymentAlreadyExistsException(requestDTO.code());

        Payment newPayment = PaymentFactory.build(requestDTO.code(),
                BigDecimal.valueOf(requestDTO.amount()).setScale(2, RoundingMode.HALF_UP),
                requestDTO.name(), requestDTO.description(), requestDTO.startDate(), requestDTO.endDate(),
                requestDTO.periodicity());

        return paymentRepository.save(newPayment);
    }

    @Override
    public void processExpiredPayments(LocalDate date) {
        log.info("Updating expired payments at {}", date);
        List<Payment> expiredPayments = paymentRepository.findAllActiveAndEndDateBefore(date);
        log.info("Found {} expired payments", expiredPayments.size());
        List<Payment> processedPaymens = ExpiredPaymentProcessor.processExpiredPayments(expiredPayments);
        log.info("Processed {} expired payments", processedPaymens.size());
        paymentRepository.saveAll(processedPaymens);
    }

    @Override
    public Payment updatePayment(UUID paymentId, UpdatePaymentRequestDTO request, LocalDate currentDate) {
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        Payment updatedPayment = payment.update(request.name(), request.description(), request.status(), currentDate);
        return paymentRepository.save(updatedPayment);
    }
}
