package com.manager.email.application.port.in;

public interface SendVerificationEmailUseCase {
    void sendVerificationEmail(String to, String verificationCode);
}
