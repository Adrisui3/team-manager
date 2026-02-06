package com.manager.email.generator;

import com.manager.email.model.Email;
import com.manager.email.model.EmailStatus;

import java.time.LocalDateTime;

public final class EmailGenerator {

    private final Email.EmailBuilder builder;

    private EmailGenerator() {
        this.builder = Email.builder();
    }

    public static EmailGenerator email() {
        EmailGenerator generator = new EmailGenerator();
        generator.toEmail("test@test.com");
        generator.subject("subject");
        generator.body("body");
        generator.createdAt(LocalDateTime.now());
        generator.status(EmailStatus.PENDING);
        return generator;
    }

    public EmailGenerator toEmail(String email) {
        builder.toEmail(email);
        return this;
    }

    public EmailGenerator subject(String subject) {
        builder.subject(subject);
        return this;
    }

    public EmailGenerator body(String body) {
        builder.body(body);
        return this;
    }

    public EmailGenerator createdAt(LocalDateTime createdAt) {
        builder.createdAt(createdAt);
        return this;
    }

    public EmailGenerator sentAt(LocalDateTime sentAt) {
        builder.sentAt(sentAt);
        return this;
    }

    public EmailGenerator status(EmailStatus status) {
        builder.status(status);
        return this;
    }

    public Email build() {
        return builder.build();
    }
}
