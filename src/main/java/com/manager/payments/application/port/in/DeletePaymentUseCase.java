package com.manager.payments.application.port.in;

import java.util.UUID;

public interface DeletePaymentUseCase {

    void deleteById(UUID id);
}
