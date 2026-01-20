package com.manager.payments.application.port.in.payments;

import com.manager.payments.adapter.in.rest.dto.request.UpdatePaymentRequestDTO;
import com.manager.payments.model.payments.Payment;

import java.time.LocalDate;
import java.util.UUID;

public interface UpdatePaymentUseCase {
    Payment updatePayment(UUID paymentId, UpdatePaymentRequestDTO request, LocalDate currentDate);
}
