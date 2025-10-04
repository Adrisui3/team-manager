package com.manager.payments.application.port.in;

import com.manager.payments.adapter.in.rest.dto.request.CreatePaymentRequestDTO;
import com.manager.payments.model.payments.Payment;

public interface CreatePaymentUseCase {
    Payment createPayment(CreatePaymentRequestDTO requestDTO);
}
