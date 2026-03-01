package com.manager.email.application.port.in;

import com.manager.email.model.ExpiredReceiptEmailRequest;

public interface SendExpiredReceiptEmailUseCase {

    void sendExpiredReceiptEmail(ExpiredReceiptEmailRequest request);

}
