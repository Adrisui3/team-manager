package com.manager.auth.model.users;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record UserVerification(UUID id, LocalDateTime expirationDate, String verificationCode, UUID userId) {

}
