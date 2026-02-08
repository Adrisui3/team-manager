package com.manager.email.application.port.out;

import com.manager.email.model.Email;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailRepository {

    void save(Email email);

    int deleteExpired(LocalDateTime date);

    List<Email> findAllToBeSent();

    void deleteAll();

    List<Email> findAll();
}
