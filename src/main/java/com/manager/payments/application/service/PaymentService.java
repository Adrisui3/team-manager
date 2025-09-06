package com.manager.payments.application.service;

import com.manager.payments.adapter.in.rest.dto.CreatePaymentRequestDTO;
import com.manager.payments.application.port.in.CreatePaymentUseCase;
import com.manager.payments.application.port.in.ProcessExpiredPaymentsUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.payments.Payment;
import com.manager.payments.model.payments.PaymentFactory;
import com.manager.payments.model.payments.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService implements CreatePaymentUseCase, ProcessExpiredPaymentsUseCase {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
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
    public void processExpiredPayments(LocalDate date) {
        logger.info("Updating expired payments at {}", date);
        List<Payment> expiredPayments = paymentRepository.findAllActiveAndEndDateBefore(date);
        logger.info("Found {} expired payments", expiredPayments.size());
        for (Payment payment : expiredPayments) {
            paymentRepository.updatePaymentStatus(payment.id(), PaymentStatus.EXPIRED);
            logger.info("Payment with id {} is now flagged as expired", payment.id());
        }
    }
}
