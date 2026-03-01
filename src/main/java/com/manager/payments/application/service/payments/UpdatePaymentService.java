package com.manager.payments.application.service.payments;

import com.manager.payments.adapter.in.rest.dto.request.UpdatePaymentRequestDTO;
import com.manager.payments.application.port.in.payments.UpdatePaymentUseCase;
import com.manager.payments.application.port.out.PaymentRepository;
import com.manager.payments.model.exceptions.PaymentNotFoundException;
import com.manager.payments.model.payments.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePaymentService implements UpdatePaymentUseCase {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment updatePayment(UUID paymentId, UpdatePaymentRequestDTO request, LocalDate currentDate) {
        Payment payment =
                paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        Payment updatedPayment = payment.update(request.name(), request.description(), request.status(), currentDate);
        return paymentRepository.save(updatedPayment);
    }
}
