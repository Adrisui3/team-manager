package com.manager.email.application.port.out;

import com.manager.email.model.Email;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailRepository {

    void save(Email email);

    List<Email> findAllToBeSent(LocalDateTime targetDate);

}
