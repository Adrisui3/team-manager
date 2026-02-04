package com.manager.email.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record Email(
        UUID id,
        String toEmail,
        String subject,
        String body,
        LocalDateTime createdAt,
        LocalDateTime sentAt,
        EmailStatus status
) {
}
