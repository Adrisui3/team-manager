package com.manager.auth.model.users;

import lombok.Builder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Builder(toBuilder = true)
public record UserVerification(UUID id, LocalDateTime expirationDate, String verificationCode, UUID userId) {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int NUM_BYTES = 16;
    private static final int USER_VERIFICATION_TIME_MINUTES = 15;

    public static UserVerification build(User user) {
        return UserVerification.builder()
                .verificationCode(generateToken())
                .expirationDate(LocalDateTime.now().plusMinutes(USER_VERIFICATION_TIME_MINUTES))
                .userId(user.id())
                .build();
    }

    private static String generateToken() {
        byte[] bytes = new byte[NUM_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
