package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.CreatePaymentRequestDTO;
import com.manager.payments.adapter.in.rest.dto.request.UpdatePaymentRequestDTO;
import com.manager.payments.application.port.in.payments.*;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentAlreadyExistsException;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements CreatePaymentUseCase, ProcessExpiredPaymentsUseCase, UpdatePaymentUseCase,
        FindPaymentUseCase, DeletePaymentUseCase {

    private final PaymentRepository repository;

    @Override
    public Payment createPayment(CreatePaymentRequestDTO requestDTO) {
        if (repository.existsByCode(requestDTO.code()))
            throw new PaymentAlreadyExistsException(requestDTO.code());

        Payment newPayment = PaymentFactory.build(requestDTO.code(),
                BigDecimal.valueOf(requestDTO.amount()).setScale(2, RoundingMode.HALF_UP),
                requestDTO.name(), requestDTO.description(), requestDTO.startDate(), requestDTO.endDate(),
                requestDTO.periodicity());

        return repository.save(newPayment);
    }

    @Override
    public void processExpiredPayments(LocalDate date) {
        log.info("Updating expired payments at {}", date);
        List<Payment> expiredPayments = repository.findAllExpired(date);
        log.info("Found {} expired payments", expiredPayments.size());
        List<Payment> processedPaymens = ExpiredPaymentProcessor.processExpiredPayments(expiredPayments);
        log.info("Processed {} expired payments", processedPaymens.size());
        repository.saveAll(processedPaymens);
    }

    @Override
    public Payment updatePayment(UUID paymentId, UpdatePaymentRequestDTO request, LocalDate currentDate) {
        Payment payment =
                repository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        Payment updatedPayment = payment.update(request.name(), request.description(), request.status(), currentDate);
        return repository.save(updatedPayment);
    }

    @Override
    public Payment findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public Page<Payment> findAll(String query, PaymentStatus status, Periodicity periodicity, Pageable pageable) {
        return repository.findAll(query.trim().toLowerCase(Locale.ROOT), status, periodicity, pageable);
    }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new PaymentNotFoundException(id);
        }

        repository.deleteById(id);
    }
}
