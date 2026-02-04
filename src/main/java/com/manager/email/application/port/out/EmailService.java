package com.manager.email.application.port.out;

import com.manager.email.model.Email;

public interface EmailService {

    void sendEmail(Email email);

}
