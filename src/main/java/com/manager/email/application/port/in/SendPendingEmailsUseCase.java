package com.manager.email.application.port.in;

import java.time.LocalDateTime;

public interface SendPendingEmailsUseCase {
    void sendPendingEmails(LocalDateTime currentDate);
}
