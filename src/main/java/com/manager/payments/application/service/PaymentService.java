package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.request.CreatePaymentRequestDTO;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.ProcessExpiredPaymentsUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentAlreadyExistsException;
import com.manager.payments.model.payments.ExpiredPaymentProcessor;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PaymentService implements CreatePaymentUseCase, ProcessExpiredPaymentsUseCase {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(CreatePaymentRequestDTO requestDTO) {
        if (paymentRepository.existsByCode(requestDTO.code()))
            throw new PaymentAlreadyExistsException(requestDTO.code());

        Payment newPayment = PaymentFactory.build(requestDTO.code(), BigDecimal.valueOf(requestDTO.amount()),
                requestDTO.name(), requestDTO.description(), requestDTO.startDate(), requestDTO.endDate(),
                requestDTO.periodicity());
        return paymentRepository.save(newPayment);
    }

    @Override
    public void processExpiredPayments(LocalDate date) {
        logger.info("Updating expired payments at {}", date);
        List<Payment> expiredPayments = paymentRepository.findAllActiveAndEndDateBefore(date);
        logger.info("Found {} expired payments", expiredPayments.size());
        List<Payment> processedPaymens = ExpiredPaymentProcessor.processExpiredPayments(expiredPayments);
        logger.info("Processed {} expired payments", processedPaymens.size());
        paymentRepository.saveAll(processedPaymens);
    }
}
