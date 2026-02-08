package com.manager.email.application.port.in;

import java.time.LocalDateTime;

public interface DeleteExpiredEmailsUseCase {

    void deleteExpiredEmails(LocalDateTime targetDate);
}
